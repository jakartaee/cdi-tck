/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.discovery.trimmed;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.BINDING_INTERCEPTOR_TO_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.INTERCEPTION_FACTORY;
import static org.jboss.cdi.tck.cdi.Sections.TRIMMED_BEAN_ARCHIVE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.Extension;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0")
public class TrimmedBeanArchiveSETest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive trimmed = ShrinkWrap.create(JavaArchive.class)
                .addClasses(TrimmedBeanArchiveSETest.class, Bar.class, Foo.class, BarProducer.class, FooProducer.class,
                        TestStereotype.class,
                        TestExtension.class, BarInterceptor.class, BarInterceptorBinding.class)
                .addAsServiceProvider(Extension.class, TestExtension.class)
                .addAsManifestResource(TrimmedBeanArchiveSETest.class.getPackage(), "beans.xml", "beans.xml");
        return ClassPath.builder().add(trimmed).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = TRIMMED_BEAN_ARCHIVE, id = "a"),
            @SpecAssertion(section = BINDING_INTERCEPTOR_TO_BEAN, id = "c"),
            @SpecAssertion(section = INTERCEPTION_FACTORY, id = "ca") })
    public void discoveredTypes() {
        try (SeContainer seContainer = SeContainerInitializer.newInstance().initialize()) {
            Bar bar = seContainer.select(Bar.class).get();
            Foo foo = seContainer.select(Foo.class).get();
            assertNotNull(foo);
            assertNotNull(bar);
            assertEquals(bar.ping(), "Intercepted Bar");

            TestExtension ext = seContainer.select(TestExtension.class).get();
            assertTrue(ext.getBarPATFired());
            assertTrue(ext.getBarProducerPBAFired());
            assertFalse(ext.getBarPBFired());
        }

    }
}
