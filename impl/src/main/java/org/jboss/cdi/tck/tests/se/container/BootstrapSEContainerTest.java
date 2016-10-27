/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.container;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;
import static org.jboss.cdi.tck.cdi.Sections.SE_CONTAINER;
import static org.jboss.cdi.tck.cdi.Sections.SE_CONTAINER_INITIALIZER;

import java.util.Optional;
import java.util.Set;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

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
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
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
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "a"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "c"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "do"),
            @SpecAssertion(section = SE_CONTAINER, id = "cb") })
    public void testContainerIsInitialized() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        SeContainer seContainer = seContainerInitializer.initialize();
        Assert.assertTrue(seContainer.isRunning());
        Foo foo = seContainer.select(Foo.class).get();
        Assert.assertNotNull(foo);
        foo.ping();
        seContainer.close();
        Assert.assertFalse(seContainer.isRunning());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertions(@SpecAssertion(section = SE_CONTAINER, id = "ca"))
    public void testContainerCloseMethodOnNotInitializedContainer() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        SeContainer seContainer = seContainerInitializer.initialize();
        seContainer.close();
        seContainer.close();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "c"), @SpecAssertion(section = SE_BOOTSTRAP, id = "da"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "do"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "e") })
    public void testInvocationOfInitializedMethodReturnsNewSeContainerInstance() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();//.initialize();
        SeContainer seContainer1 = seContainerInitializer.initialize();
        Assert.assertNotNull(seContainer1);
        seContainer1.close();

        SeContainer seContainer2 = seContainerInitializer.initialize();
        Assert.assertNotNull(seContainer2);
        seContainer1.close();
        Assert.assertNotEquals(seContainer1, seContainer2);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "db"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dm"),
            @SpecAssertion(section = SE_CONTAINER, id = "a"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testSyntheticArchive() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery().addBeanClasses(Baz.class, Qux.class, BazObserver.class).initialize()) {
            BeanManager beanManager = seContainer.getBeanManager();
            beanManager.fireEvent(new Baz(), Any.Literal.INSTANCE);
            beanManager.fireEvent(new Qux(), Any.Literal.INSTANCE);
            Assert.assertNotNull(seContainer.select(Baz.class).get().ping());
            Assert.assertTrue(BazObserver.isNotified);
            // is not in synthetic archive
            Assert.assertFalse(QuxObserver.isNotified);
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "db"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dh"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "di"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dm"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAlternativesInSE() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Square.class, Circle.class, Foo.class, FooProducer.class)
                .addAlternatives(Circle.class)
                .addAlternativeStereotypes(AlternativeStereotype.class)
                .initialize()) {
            Shape shape = seContainer.select(Shape.class).get();
            Assert.assertEquals(shape.name(), Circle.NAME);
            Set<Bean<?>> foos = seContainer.getBeanManager().getBeans(Foo.class);
            Optional<Bean<?>> alternativeFoo = foos.stream().filter(bean -> bean.isAlternative()).findAny();
            Assert.assertTrue(alternativeFoo.isPresent());
            Assert.assertEquals(alternativeFoo.get().getName(), "createFoo");
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "dc"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddPackageNotRecursively() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addPackages(Apple.class.getPackage())
                .initialize()) {
            Instance<Apple> appleInstance = seContainer.select(Apple.class);
            Instance<Pear> pearInstance = seContainer.select(Pear.class);
            Assert.assertFalse(appleInstance.isUnsatisfied());
            Assert.assertTrue(pearInstance.isUnsatisfied());
            Assert.assertNotNull(appleInstance.get().getWorm());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "dc"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddPackageRecursively() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addPackages(true, Apple.class.getPackage())
                .initialize()) {
            Instance<Apple> appleInstance = seContainer.select(Apple.class);
            Instance<Pear> pearInstance = seContainer.select(Pear.class);
            Assert.assertFalse(appleInstance.isUnsatisfied());
            Assert.assertFalse(pearInstance.isUnsatisfied());
            Assert.assertNotNull(appleInstance.get().getWorm());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "de"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddExtensionAsExtensionInstance() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        TestExtension testExtension = new TestExtension();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Foo.class)
                .addExtensions(testExtension)
                .initialize()) {
            TestExtension containerExtension = seContainer.select(TestExtension.class).get();
            Assert.assertTrue(containerExtension.getBeforeBeanDiscoveryNotified().get());
            Assert.assertTrue(containerExtension.getAfterTypeDiscoveryNotified().get());
            Assert.assertTrue(containerExtension.getAfterBeanDiscoveryNotified().get());
            Assert.assertTrue(containerExtension.getAfterDeploymentValidationNotified().get());
            Assert.assertTrue(containerExtension.getProcessAnnotatedTypeNotified().get());
            Assert.assertTrue(containerExtension.getProcessInjectionTargetNotified().get());
            Assert.assertTrue(containerExtension.getProcessBeanAttributesNotified().get());
            Assert.assertTrue(containerExtension.getProcessBeanNotified().get());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "de"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddExtensionAsClass() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Foo.class)
                .addExtensions(TestExtension.class)
                .initialize()) {
            TestExtension containerExtension = seContainer.select(TestExtension.class).get();
            Assert.assertTrue(containerExtension.getBeforeBeanDiscoveryNotified().get());
            Assert.assertTrue(containerExtension.getAfterTypeDiscoveryNotified().get());
            Assert.assertTrue(containerExtension.getAfterBeanDiscoveryNotified().get());
            Assert.assertTrue(containerExtension.getAfterDeploymentValidationNotified().get());
            Assert.assertTrue(containerExtension.getProcessAnnotatedTypeNotified().get());
            Assert.assertTrue(containerExtension.getProcessInjectionTargetNotified().get());
            Assert.assertTrue(containerExtension.getProcessBeanAttributesNotified().get());
            Assert.assertTrue(containerExtension.getProcessBeanNotified().get());
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "df"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddInterceptor() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Bar.class, BarInterceptor1.class, BarInterceptor2.class)
                .addInterceptors(BarInterceptor1.class, BarInterceptor2.class)
                .initialize()) {
            Bar bar = seContainer.select(Bar.class).get();
            int result = bar.ping();
            Assert.assertTrue(BarInterceptor1.notified);
            Assert.assertTrue(BarInterceptor2.notified);
            Assert.assertEquals(result, 3);

        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "dg"), @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddDecorator() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Corge.class, CorgeImpl.class, CorgeDecorator.class)
                .addDecorators(CorgeDecorator.class)
                .initialize()) {
            Corge corge = seContainer.select(Corge.class).get();
            int result = corge.ping();
            Assert.assertTrue(CorgeDecorator.notified);
            Assert.assertEquals(2, result);
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = SE_CONTAINER, id = "cc")
    public void seContainerThrowsISAWhenAccessingBmAtShutdownContainer(){
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        SeContainer seContainer = seContainerInitializer.initialize();
        seContainer.close();
        Assert.assertFalse(seContainer.isRunning());
        BeanManager bm = seContainer.getBeanManager();
    }

}
