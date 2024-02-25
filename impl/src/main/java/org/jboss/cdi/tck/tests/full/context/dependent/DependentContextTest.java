/*
 * Copyright 2022, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.context.dependent;

import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_CONTEXT_EE;
import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_DESTRUCTION_EE;

import java.util.Set;

import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = TestGroups.CDI_FULL)
public class DependentContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DependentContextTest.class).build();
    }

    @Test
    @SpecAssertion(section = DEPENDENT_CONTEXT_EE, id = "ca")
    public void testInstanceUsedForElEvaluationNotShared() throws Exception {
        Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
        assert foxBeans.size() == 1;

        Fox fox1 = getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{fox}", Fox.class);
        Fox fox2 = getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{fox}", Fox.class);
        assert !fox1.equals(fox2);
    }

    @Test
    @SpecAssertion(section = DEPENDENT_DESTRUCTION_EE, id = "eee")
    public void testDependentsDestroyedWhenElEvaluationCompletes() throws Exception {
        // Reset test class
        Fox.reset();
        FoxRun.setDestroyed(false);

        getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{foxRun}", FoxRun.class);
        assert FoxRun.isDestroyed();
        assert Fox.isDestroyed();
    }

    @Test
    @SpecAssertion(section = DEPENDENT_CONTEXT, id = "g")
    // Dependent context is now always active
    public void testContextIsActiveWhenEvaluatingElExpression() {
        String foxName = getCurrentConfiguration().getEl().evaluateMethodExpression(getCurrentManager(),
                "#{sensitiveFox.getName}", String.class, new Class[0], new Object[0]);
        assert foxName != null;
        assert SensitiveFox.isDependentContextActiveDuringEval();
    }
}
