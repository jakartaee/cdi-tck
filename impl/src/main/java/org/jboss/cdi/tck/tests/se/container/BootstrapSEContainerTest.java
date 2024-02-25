/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.container;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;
import static org.jboss.cdi.tck.cdi.Sections.SE_CONTAINER;
import static org.jboss.cdi.tck.cdi.Sections.SE_CONTAINER_INITIALIZER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Optional;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.tests.se.container.testPackage.Apple;
import org.jboss.cdi.tck.tests.se.container.testPackage.Worm;
import org.jboss.cdi.tck.tests.se.container.testPackage.nestedPackage.Pear;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0")
public class BootstrapSEContainerTest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class)
                .addPackage(BootstrapSEContainerTest.class.getPackage())
                .addClasses(Apple.class, Worm.class, Pear.class)
                .addAsResource(EmptyAsset.INSTANCE,
                        "META-INF/beans.xml");
        return ClassPath.builder().add(testArchive).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "a"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "a"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "c"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "do"),
            @SpecAssertion(section = SE_CONTAINER, id = "cb") })
    public void testContainerIsInitialized() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        SeContainer seContainer = seContainerInitializer.initialize();
        assertTrue(seContainer.isRunning());
        Foo foo = seContainer.select(Foo.class).get();
        assertNotNull(foo);
        foo.ping();
        seContainer.close();
        assertFalse(seContainer.isRunning());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertions(@SpecAssertion(section = SE_CONTAINER, id = "ca"))
    public void testContainerCloseMethodOnNotInitializedContainer() {
        SeContainer seContainer = initializeAndShutdownContainer();
        seContainer.close();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "a"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "c"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "da"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "do"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "e") })
    public void testInvocationOfInitializedMethodReturnsNewSeContainerInstance() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        SeContainer seContainer1 = seContainerInitializer.initialize();
        assertNotNull(seContainer1);
        seContainer1.close();

        SeContainer seContainer2 = seContainerInitializer.initialize();
        assertNotNull(seContainer2);
        seContainer2.close();
        assertNotEquals(seContainer1, seContainer2);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "db"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dm"),
            @SpecAssertion(section = SE_CONTAINER, id = "a"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testSyntheticArchive() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Baz.class, Qux.class, BazObserver.class).initialize()) {
            BeanManager beanManager = seContainer.getBeanManager();
            beanManager.getEvent().select(Baz.class, Any.Literal.INSTANCE).fire(new Baz());
            beanManager.getEvent().select(Qux.class, Any.Literal.INSTANCE).fire(new Qux());
            assertNotNull(seContainer.select(Baz.class).get().ping());
            assertTrue(BazObserver.isNotified);
            // is not in synthetic archive
            assertFalse(QuxObserver.isNotified);
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "a"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "db"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "dh"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "di"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dm"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAlternativesInSE() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Square.class, Circle.class, Foo.class, FooProducer.class)
                .selectAlternatives(Circle.class)
                .selectAlternativeStereotypes(AlternativeStereotype.class)
                .initialize()) {
            Shape shape = seContainer.select(Shape.class).get();
            assertEquals(shape.name(), Circle.NAME);
            Set<Bean<?>> foos = seContainer.getBeanManager().getBeans(Foo.class);
            Optional<Bean<?>> alternativeFoo = foos.stream().filter(bean -> bean.isAlternative()).findAny();
            assertTrue(alternativeFoo.isPresent());
            assertEquals(alternativeFoo.get().getName(), "createFoo");
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "dc"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddPackageNotRecursively() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addPackages(Apple.class.getPackage())
                .initialize()) {
            Instance<Apple> appleInstance = seContainer.select(Apple.class);
            Instance<Pear> pearInstance = seContainer.select(Pear.class);
            assertFalse(appleInstance.isUnsatisfied());
            assertTrue(pearInstance.isUnsatisfied());
            assertNotNull(appleInstance.get().getWorm());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "dc"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddPackageRecursively() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addPackages(true, Apple.class.getPackage())
                .initialize()) {
            Instance<Apple> appleInstance = seContainer.select(Apple.class);
            Instance<Pear> pearInstance = seContainer.select(Pear.class);
            assertFalse(appleInstance.isUnsatisfied());
            assertFalse(pearInstance.isUnsatisfied());
            assertNotNull(appleInstance.get().getWorm());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "de"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddExtensionAsExtensionInstance() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        TestExtension testExtension = new TestExtension();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Foo.class)
                .addExtensions(testExtension)
                .initialize()) {
            TestExtension containerExtension = seContainer.select(TestExtension.class).get();
            assertTrue(containerExtension.getBeforeBeanDiscoveryNotified().get());
            assertTrue(containerExtension.getAfterTypeDiscoveryNotified().get());
            assertTrue(containerExtension.getAfterBeanDiscoveryNotified().get());
            assertTrue(containerExtension.getAfterDeploymentValidationNotified().get());
            assertTrue(containerExtension.getProcessAnnotatedTypeNotified().get());
            assertTrue(containerExtension.getProcessInjectionTargetNotified().get());
            assertTrue(containerExtension.getProcessBeanAttributesNotified().get());
            assertTrue(containerExtension.getProcessBeanNotified().get());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "de"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddExtensionAsClass() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Foo.class)
                .addExtensions(TestExtension.class)
                .initialize()) {
            TestExtension containerExtension = seContainer.select(TestExtension.class).get();
            assertTrue(containerExtension.getBeforeBeanDiscoveryNotified().get());
            assertTrue(containerExtension.getAfterTypeDiscoveryNotified().get());
            assertTrue(containerExtension.getAfterBeanDiscoveryNotified().get());
            assertTrue(containerExtension.getAfterDeploymentValidationNotified().get());
            assertTrue(containerExtension.getProcessAnnotatedTypeNotified().get());
            assertTrue(containerExtension.getProcessInjectionTargetNotified().get());
            assertTrue(containerExtension.getProcessBeanAttributesNotified().get());
            assertTrue(containerExtension.getProcessBeanNotified().get());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "df"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddInterceptor() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Bar.class, BarInterceptor1.class, BarInterceptor2.class)
                .enableInterceptors(BarInterceptor1.class, BarInterceptor2.class)
                .initialize()) {
            Bar bar = seContainer.select(Bar.class).get();
            int result = bar.ping();
            assertTrue(BarInterceptor1.notified);
            assertTrue(BarInterceptor2.notified);
            assertEquals(result, 3);

        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "dg"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddDecorator() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Corge.class, CorgeImpl.class, CorgeDecorator.class)
                .enableDecorators(CorgeDecorator.class)
                .initialize()) {
            Corge corge = seContainer.select(Corge.class).get();
            int result = corge.ping();
            assertTrue(CorgeDecorator.notified);
            assertEquals(result, 2);
        }
    }

    @Test
    @SpecAssertion(section = SE_CONTAINER, id = "d")
    public void testSeContainerLookup() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.initialize()) {
            Instance<Garply> garplyInstance = seContainer.select(Garply.class);
            assertTrue(garplyInstance.isResolvable());
            assertEquals(garplyInstance.get().getNumber(), 0);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = SE_CONTAINER, id = "cc")
    public void seContainerThrowsISEWhenAccessingBmAtShutdownContainer() {
        SeContainer seContainer = initializeAndShutdownContainer();
        BeanManager bm = seContainer.getBeanManager();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = SE_CONTAINER, id = "e")
    public void instanceSelectClassThrowsISEWhenAccessedAfterShutdown() {
        SeContainer seContainer = initializeAndShutdownContainer();
        seContainer.select(Corge.class);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = SE_CONTAINER, id = "e")
    public void instanceSelectAnnotationThrowsISEWhenAccessedAfterShutdown() {
        SeContainer seContainer = initializeAndShutdownContainer();
        seContainer.select(InjectLiteral.INSTANCE);
    }

    private SeContainer initializeAndShutdownContainer() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        SeContainer seContainer = seContainerInitializer.initialize();
        seContainer.close();
        assertFalse(seContainer.isRunning());
        return seContainer;
    }

}
