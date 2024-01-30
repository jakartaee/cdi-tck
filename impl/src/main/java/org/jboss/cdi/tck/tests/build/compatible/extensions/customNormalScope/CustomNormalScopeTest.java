/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

// this test is basically a stripped down version of https://github.com/weld/command-context-example
public class CustomNormalScopeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomNormalScopeTest.class)
                .withBuildCompatibleExtension(CustomNormalScopeExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "Register custom normal scope")
    public void commandContextController() {
        CommandContextController control = getContextualReference(CommandContextController.class);
        boolean activated = control.activate();
        assertTrue(activated);
        try {
            assertEquals(getContextualReference(IdService.class).get(), getContextualReference(IdService.class).get());
        } finally {
            control.deactivate();
        }
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "Register custom normal scope")
    public void commandExecutor() {
        CommandExecutor executor = getContextualReference(CommandExecutor.class);
        executor.execute(() -> {
            CommandExecution execution = getContextualReference(CommandExecution.class);
            IdService idService = getContextualReference(IdService.class);

            getContextualReference(MyService.class).process();
            assertEquals(execution.getData().get("id"), idService.get());
            assertNotNull(execution.getStartedAt());
        });
    }
}
