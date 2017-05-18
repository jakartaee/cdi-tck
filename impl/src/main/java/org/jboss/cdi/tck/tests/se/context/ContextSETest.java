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
package org.jboss.cdi.tck.tests.se.context;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

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
@SpecVersion(spec = "cdi", version = "2.0")
public class ContextSETest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(ContextSETest.class, ApplicationScopedCounter.class, ApplicationScopedObserver.class)
                .addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        final JavaArchive fooArchive = ShrinkWrap.create(JavaArchive.class).addClasses(Foo.class).addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        final JavaArchive barArchive = ShrinkWrap.create(JavaArchive.class).addClasses(Bar.class).addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        final JavaArchive bazArchive = ShrinkWrap.create(JavaArchive.class).addClasses(Baz.class).addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        return ClassPath.builder().add(testArchive, fooArchive, barArchive, bazArchive).build();
    }

    @Test
    @SpecAssertion(section = APPLICATION_CONTEXT_SE, id = "a")
    public void applicationContextSharedBetweenAllBeansWithinContainer() {
        try (SeContainer seContainer = SeContainerInitializer.newInstance().initialize()) {
            seContainer.select(Foo.class).get().ping();
            seContainer.select(Bar.class).get().ping();
            seContainer.select(Baz.class).get().ping();

            ApplicationScopedCounter applicationScopedCounter = seContainer.select(ApplicationScopedCounter.class).get();
            Assert.assertEquals(applicationScopedCounter.getCount(), 3);
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = APPLICATION_CONTEXT_SE, id = "b"), @SpecAssertion(section = APPLICATION_CONTEXT_SE, id = "d"),
            @SpecAssertion(section = SE_BOOTSTRAP, id = "f"), @SpecAssertion(section = SE_BOOTSTRAP, id = "f"),
            @SpecAssertion(section = APPLICATION_CONTEXT, id = "a")})
    public void testEventIsFiredWhenAplicationContextInitialized() {
        ApplicationScopedObserver.reset();
        try (SeContainer seContainer = SeContainerInitializer.newInstance().initialize()) {
            Assert.assertTrue(ApplicationScopedObserver.isInitialized);
            Assert.assertNotNull(ApplicationScopedObserver.initializedEventPayload);
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = APPLICATION_CONTEXT_SE, id = "c"), @SpecAssertion(section = APPLICATION_CONTEXT_SE, id = "d"),
            @SpecAssertion(section = APPLICATION_CONTEXT, id = "b"), @SpecAssertion(section = APPLICATION_CONTEXT, id = "c") })
    public void testEventIsFiredWhenAplicationContextDestroyed() {
        ApplicationScopedObserver.reset();
        try (SeContainer seContainer = SeContainerInitializer.newInstance().initialize()) {

        }
        Assert.assertTrue(ApplicationScopedObserver.isBeforeDestroyed);
        Assert.assertNotNull(ApplicationScopedObserver.beforeDestroyedEventPayload);
        Assert.assertTrue(ApplicationScopedObserver.isDestroyed);
        Assert.assertNotNull(ApplicationScopedObserver.destroyedEventPayload);
    }

}
