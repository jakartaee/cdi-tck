/*
 * Copyright 2022, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.events.lifecycle;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Shutdown;
import jakarta.enterprise.event.Startup;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests {@link jakarta.enterprise.event.Startup} and {@link jakarta.enterprise.event.Shutdown} event delivery.
 * The reason we test this in SE container is that we can make assertions on both of these events as the test
 * spans even beyond the CDI container lifecycle.
 */
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "4.0")
public class StartupShutdownTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ClassPath.builder().add(ShrinkWrap.create(JavaArchive.class)
                .addPackage(StartupShutdownTest.class.getPackage())).build();
    }

    @Test
    @SpecAssertion(section = Sections.STARTUP_EVENT, id = "aa")
    @SpecAssertion(section = Sections.STARTUP_EVENT, id = "ab")
    @SpecAssertion(section = Sections.SHUTDOWN_EVENT, id = "aa")
    @SpecAssertion(section = Sections.SHUTDOWN_EVENT, id = "ab")
    public void testEvents() {
        // assert initial state
        Assert.assertTrue(ObservingBean.OBSERVED_STARTING_EVENTS.isEmpty());
        Assert.assertTrue(ObservingBean.OBSERVED_SHUTDOWN_EVENTS.isEmpty());

        try (SeContainer seContainer = SeContainerInitializer.newInstance().addBeanClasses(ObservingBean.class).initialize()) {
            Assert.assertTrue(ObservingBean.OBSERVED_STARTING_EVENTS.size() == 2);
            Assert.assertTrue(ObservingBean.OBSERVED_STARTING_EVENTS.get(0).equals(ApplicationScoped.class.getSimpleName()));
            Assert.assertTrue(ObservingBean.OBSERVED_STARTING_EVENTS.get(1).equals(Startup.class.getSimpleName()));
        }

        // we can only assert these events after we shutdown Weld container
        Assert.assertTrue(ObservingBean.OBSERVED_SHUTDOWN_EVENTS.size() == 2);
        Assert.assertTrue(ObservingBean.OBSERVED_SHUTDOWN_EVENTS.get(0).equals(Shutdown.class.getSimpleName()));
        Assert.assertTrue(ObservingBean.OBSERVED_SHUTDOWN_EVENTS.get(1).equals(ApplicationScoped.class.getSimpleName()));

    }
}
