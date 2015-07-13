/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015 Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.extensions.observer.priority;

import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the extensions provided by the ProcessObserverMethod events.
 * 
 * @author Mark Paluch
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class ExtensionObserverOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ExtensionObserverOrderingTest.class)
                .withExtension(PrioritizedExtensionEvents.class).build();
    }
    
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INIT_EVENTS, id = "e")
    public void testEventOrdering(PrioritizedExtensionEvents prioritizedExtensionEvents) {
        List<String> notificationOrder = prioritizedExtensionEvents.getNotificationOrder();

        assertEquals(notificationOrder.size(), 3);
        assertEquals(notificationOrder.get(0), PrioritizedExtensionEvents.EARLY);
        assertEquals(notificationOrder.get(1), PrioritizedExtensionEvents.MIDDLE);
        assertEquals(notificationOrder.get(2), PrioritizedExtensionEvents.LATE);
    }
}
