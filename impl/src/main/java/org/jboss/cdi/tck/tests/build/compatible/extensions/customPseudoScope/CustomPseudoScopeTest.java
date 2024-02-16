/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

@SpecVersion(spec = "cdi", version = "4.0")
public class CustomPseudoScopeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomPseudoScopeTest.class)
                .withBuildCompatibleExtension(CustomPseudoScopeExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "Register custom pseudo-scope")
    public void test() {
        PrototypeBean prototypeBean = getContextualReference(PrototypeBean.class);

        assertNotEquals(getContextualReference(PrototypeBean.class).getId(), prototypeBean.getId());

        ApplicationScopedBean applicationScopedBean = getContextualReference(ApplicationScopedBean.class);
        assertEquals(getContextualReference(ApplicationScopedBean.class).getPrototypeId(),
                applicationScopedBean.getPrototypeId());
        assertNotEquals(prototypeBean.getId(), applicationScopedBean.getPrototypeId());

        RequestScopedBean requestScopedBean = getContextualReference(RequestScopedBean.class);
        assertEquals(getContextualReference(RequestScopedBean.class).getPrototypeId(),
                requestScopedBean.getPrototypeId());
        assertNotEquals(prototypeBean.getId(), requestScopedBean.getPrototypeId());

        DependentBean dependentBean = getContextualReference(DependentBean.class);
        assertNotEquals(getContextualReference(DependentBean.class).getPrototypeId(),
                dependentBean.getPrototypeId());
        assertNotEquals(prototypeBean.getId(), dependentBean.getPrototypeId());
    }
}
