/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_RESOLUTION;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = INTEGRATION)
public class ObserverNotificationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverNotificationTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_RESOLUTION, id = "c"),
            @SpecAssertion(section = OBSERVER_RESOLUTION, id = "d"), @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "aa") })
    public void testObserversNotified() {

        AnEventType anEvent = new AnEventType();

        resetObservers();
        // Fire an event with qualifiers @Role("Admin") and @Any
        getCurrentManager().getEvent().select(AnEventType.class, new RoleLiteral("Admin", "hurray")).fire(anEvent);

        assertTrue(AnObserver.wasNotified);
        assertTrue(AnotherObserver.wasNotified);
        assertTrue(LastObserver.wasNotified);
        assertFalse(DisabledObserver.wasNotified);

        resetObservers();
        // Fire an event with qualifier @Any
        getCurrentManager().getEvent().select(AnEventType.class).fire(anEvent);
        assertTrue(AnObserver.wasNotified);
        assertFalse(AnotherObserver.wasNotified);
        assertTrue(LastObserver.wasNotified);
        assertFalse(DisabledObserver.wasNotified);

        resetObservers();
        // Fire an event with qualifiers @Role("user") and @Any
        getCurrentManager().getEvent().select(AnEventType.class, new RoleLiteral("user", "hurray")).fire(anEvent);
        assertTrue(AnObserver.wasNotified);
        assertFalse(AnotherObserver.wasNotified);
        assertTrue(LastObserver.wasNotified);
        assertFalse(DisabledObserver.wasNotified);
    }

    @Test
    @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "bca")
    public void testObserverMethodNotInvokedIfNoActiveContext() {

        Context requestContext = getCurrentConfiguration().getContexts().getRequestContext();

        resetObservers();

        try {
            setContextInactive(requestContext);
            // Observer method not called - there is no context active for its scope
            getCurrentManager().getEvent().select(AnEventType.class, new RoleLiteral("Admin", "hurray")).fire(new AnEventType());
            assertFalse(AnotherObserver.wasNotified);
        } finally {
            setContextActive(requestContext);
        }
    }

    private void resetObservers() {
        AnObserver.wasNotified = false;
        AnotherObserver.wasNotified = false;
        LastObserver.wasNotified = false;
    }

}
