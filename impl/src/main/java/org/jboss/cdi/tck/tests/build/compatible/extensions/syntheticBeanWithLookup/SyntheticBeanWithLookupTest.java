/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanWithLookup;

import static org.testng.Assert.assertEquals;

import jakarta.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class SyntheticBeanWithLookupTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        // no beans.xml + an extension = not a bean archive, bean classes are added through the extension
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticBeanWithLookupTest.class)
                .withBuildCompatibleExtension(SyntheticBeanWithLookupExtension.class)
                .withoutBeansXml()
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "c")
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "a")
    public void test() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();

        assertEquals(0, MyPojo.createdCounter.get());
        assertEquals(0, MyPojo.destroyedCounter.get());
        assertEquals(0, MyPojoCreator.counter.get());
        assertEquals(0, MyPojoDisposer.counter.get());
        assertEquals(0, MyDependentBean.createdCounter.get());
        assertEquals(0, MyDependentBean.destroyedCounter.get());

        Instance.Handle<MyPojo> bean = lookup.select(MyPojo.class).getHandle();
        assertEquals("Hello!", bean.get().hello());

        assertEquals(1, MyPojo.createdCounter.get());
        assertEquals(0, MyPojo.destroyedCounter.get());
        assertEquals(1, MyPojoCreator.counter.get());
        assertEquals(0, MyPojoDisposer.counter.get());
        assertEquals(1, MyDependentBean.createdCounter.get());
        assertEquals(0, MyDependentBean.destroyedCounter.get());

        bean.destroy();

        assertEquals(1, MyPojo.createdCounter.get());
        assertEquals(1, MyPojo.destroyedCounter.get());
        assertEquals(1, MyPojoCreator.counter.get());
        assertEquals(1, MyPojoDisposer.counter.get());
        assertEquals(2, MyDependentBean.createdCounter.get());
        assertEquals(2, MyDependentBean.destroyedCounter.get());
    }
}
