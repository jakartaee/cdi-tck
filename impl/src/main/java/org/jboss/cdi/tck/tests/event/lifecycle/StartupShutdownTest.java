/*
 * Copyright 2022, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.lifecycle;

import static org.jboss.cdi.tck.cdi.Sections.STARTUP_EVENT;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Startup;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests {@link jakarta.enterprise.event.Startup} event. The test also uses {@link jakarta.enterprise.event.Shutdown}
 * but we cannot make assertion on it because the entirety of the test happens inside running CDI container.
 *
 * Note that there is also {@link org.jboss.cdi.tck.tests.se.events.lifecycle.StartupShutdownTest} which tests
 * both events under CDI SE.
 */
@SpecVersion(spec = "cdi", version = "4.0")
public class StartupShutdownTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(StartupShutdownTest.class).build();
    }

    @Test
    @SpecAssertion(section = STARTUP_EVENT, id = "aa")
    @SpecAssertion(section = STARTUP_EVENT, id = "ab")
    public void testEventsObserved() {
        Assert.assertTrue(ObservingBean.OBSERVED_STARTING_EVENTS.size() == 2);
        Assert.assertTrue(ObservingBean.OBSERVED_STARTING_EVENTS.get(0).equals(ApplicationScoped.class.getSimpleName()));
        Assert.assertTrue(ObservingBean.OBSERVED_STARTING_EVENTS.get(1).equals(Startup.class.getSimpleName()));

        // Note that we cannot assert that shutdown event was invoked because entirety of this test class
        // happens before shutdown
        Assert.assertTrue(ObservingBean.OBSERVED_SHUTDOWN_EVENTS.isEmpty());
    }
}
