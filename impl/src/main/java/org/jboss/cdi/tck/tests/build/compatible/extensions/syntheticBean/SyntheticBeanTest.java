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
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

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
public class SyntheticBeanTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticBeanTest.class)
                .withBuildCompatibleExtension(SyntheticBeanExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "a")
    public void test() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();

        Instance.Handle<MyService> handle = lookup.select(MyService.class).getHandle();
        MyService myService = handle.get();

        {
            assertEquals("Hello World", myService.unqualified.text);

            MyComplexValue ann = myService.unqualified.ann;
            assertNotNull(ann);
            assertEquals(42, ann.number());
            assertEquals(MyEnum.YES, ann.enumeration());
            assertEquals(MyEnum.class, ann.type());
            assertEquals("yes", ann.nested().value());
            assertEquals(new byte[] { 4, 5, 6 }, ann.nested().bytes());
        }

        {
            assertEquals("Hello @MyQualifier Special", myService.qualified.text);

            MyComplexValue ann = myService.qualified.ann;
            assertNotNull(ann);
            assertEquals(13, ann.number());
            assertEquals(MyEnum.NO, ann.enumeration());
            assertEquals(MyEnum.class, ann.type());
            assertEquals("no", ann.nested().value());
            assertEquals(new byte[] { 1, 2, 3 }, ann.nested().bytes());
        }

        assertEquals(0, MyPojoDisposer.disposed.size());

        handle.destroy();

        assertEquals(2, MyPojoDisposer.disposed.size());
        assertTrue(MyPojoDisposer.disposed.contains("Hello World"));
        assertTrue(MyPojoDisposer.disposed.contains("Hello @MyQualifier Special"));
    }
}
