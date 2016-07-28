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
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;
import static org.jboss.cdi.tck.cdi.Sections.SE_CONTAINER;

import java.util.Optional;
import java.util.Set;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
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
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class BootstrapSEContainerTest extends Arquillian {

    private static final String IMPLICIT_SCAN_KEY = "javax.enterprise.inject.scan.implicit";

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(Foo.class, Baz.class, Qux.class, BazObserver.class, QuxObserver.class, Shape.class, Square.class, Circle.class,
                        BootstrapSEContainerTest.class)
                .addAsResource(EmptyAsset.INSTANCE,
                        "META-INF/beans.xml");
        final JavaArchive implicitArchive = ShrinkWrap.create(JavaArchive.class).addClass(Bar.class);
        return ClassPath.builder().add(testArchive, implicitArchive).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "a"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "b"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "c"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "dn"),
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
            @SpecAssertion(section = SE_BOOTSTRAP, id = "dn"),
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
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_SE, id = "b"), @SpecAssertion(section = SE_BOOTSTRAP, id = "di") })
    public void testImplicitArchiveDiscovered() {
        try (SeContainer seContainer = SeContainerInitializer.newInstance().addProperty(IMPLICIT_SCAN_KEY, true).initialize()) {
            Bar bar = seContainer.select(Bar.class).get();
            Assert.assertNotNull(bar);
            bar.ping();
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "db"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dl"),
            @SpecAssertion(section = SE_CONTAINER, id = "a") })
    public void testSyntheticArchive() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        SeContainer seContainer = seContainerInitializer.disableDiscovery().addBeanClasses(Baz.class, Qux.class, BazObserver.class).initialize();
        BeanManager beanManager = seContainer.getBeanManager();
        beanManager.fireEvent(new Baz(), Any.Literal.INSTANCE);
        beanManager.fireEvent(new Qux(), Any.Literal.INSTANCE);
        Assert.assertNotNull(seContainer.select(Baz.class).get().ping());
        Assert.assertTrue(BazObserver.isNotified);
        // is not in synthetic archive
        Assert.assertFalse(QuxObserver.isNotified);
        seContainer.close();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "db"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dg"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "dh"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dl") })
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

}
