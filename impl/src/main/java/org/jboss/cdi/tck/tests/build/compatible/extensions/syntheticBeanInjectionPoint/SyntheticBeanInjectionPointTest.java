/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionPoint;

import jakarta.enterprise.inject.Instance;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

@SpecVersion(spec = "cdi", version = "4.0")
public class SyntheticBeanInjectionPointTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticBeanInjectionPointTest.class)
                .withBuildCompatibleExtension(SyntheticBeanInjectionPointExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "a")
    public void test() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();

        // For historical reasons, the spec is a little loose on what should happen
        // when looking up an `InjectionPoint` in a synthetic bean creation function
        // of a non-`@Dependent` bean, or in a synthetic bean destruction function.
        // Realistically, 2 different things may happen: the implementation returns
        // `null`, or it throws an exception. Further, that exception may be swallowed
        // by the `Instance.Handle#get` or `destroy` method. The only thing we can
        // possibly test for is whether storing the `InjectionPoint` lookup result
        // into a `static` field, whose previous value was `null`, changed that value.

        Instance.Handle<MyDependentBean> handle = lookup.select(MyDependentBean.class).getHandle();
        try {
            handle.get();
        } catch (Exception e) {
            fail();
        }
        assertNotNull(MyDependentBeanCreator.lookedUp);

        try {
            handle.destroy();
        } catch (Exception ignored) {
        }
        assertNull(MyDependentBeanDisposer.lookedUp);

        try {
            lookup.select(MyApplicationScopedBean.class).get();
        } catch (Exception ignored) {
        }
        assertNull(MyApplicationScopedBeanCreator.lookedUp);
    }
}
