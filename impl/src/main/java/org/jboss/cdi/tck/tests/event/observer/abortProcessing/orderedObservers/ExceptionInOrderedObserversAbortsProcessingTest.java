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
package org.jboss.cdi.tck.tests.event.observer.abortProcessing.orderedObservers;

import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_ORDERING;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class ExceptionInOrderedObserversAbortsProcessingTest extends AbstractTest {

    @Inject
    Event<Invitation> event;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ExceptionInOrderedObserversAbortsProcessingTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "cd"),
            @SpecAssertion(section = OBSERVER_ORDERING, id = "b") })
    public void testOrderedObserversAbortedCorrectly() {

        try {
            event.fire(new Invitation());
        } catch (OrderedObservers.CancelledException e) {
            assertTrue(OrderedObservers.familyInvited);
            assertTrue(OrderedObservers.bestFriendsInvited);
            assertFalse(OrderedObservers.goodFriendsInvited);
            assertFalse(OrderedObservers.othersInvited);
        }
    }
}
