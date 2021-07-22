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
package org.jboss.cdi.tck.tests.event;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBSERVER_METHOD_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT_DEFAULT_QUALIFIER;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.jboss.cdi.tck.cdi.Sections.METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.MULTIPLE_EVENT_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVERS_METHOD_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHODS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVES;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import jakarta.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Event bus tests
 *
 * @author Nicklas Karlsson
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EventTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVES, id = "i"), @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "c"),
            @SpecAssertion(section = METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS, id = "ca"),
            @SpecAssertion(section = INJECTION_POINT_DEFAULT_QUALIFIER, id = "a") })
    public void testObserverMethodParameterInjectionPoints() {
        TerrierObserver.reset();
        getCurrentManager().getEvent().select(BullTerrier.class).fire(new BullTerrier());
        assertTrue(TerrierObserver.eventObserved);
        assertTrue(TerrierObserver.parametersInjected);
    }

    /**
     * This test was temporarily marked as integration one because of problems with arquillian-weld-ee-embedded-1.1 container
     * adapter.
     */
    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHODS, id = "c"), @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "a") })
    public void testStaticObserverMethodInvoked() {

        Context requestContext = getCurrentConfiguration().getContexts().getRequestContext();

        try {
            // Deactivate request context so that we're sure the contextual instance is not obtained
            getCurrentConfiguration().getContexts().setInactive(requestContext);

            StaticObserver.reset();
            getCurrentManager().getEvent().select(Delivery.class).fire(new Delivery());

            assertTrue(StaticObserver.isDeliveryReceived());

        } finally {
            getCurrentConfiguration().getContexts().setActive(requestContext);
        }
    }

    @Test
    @SpecAssertion(section = OBSERVES, id = "a")
    public void testPrivateObserverMethodInvoked() {
        PrivateObserver.reset();
        getCurrentManager().getEvent().select(Delivery.class).fire(new Delivery());
        assertTrue(PrivateObserver.isObserved);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SPECIALIZATION, id = "cc"), @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "baa") })
    public void testObserverCalledOnSpecializedBeanOnly() {
        Shop.observers.clear();
        getCurrentManager().getEvent().select(Delivery.class).fire(new Delivery());
        // FarmShop specializes Shop
        assertEquals(Shop.observers.size(), 1);
        assertEquals(Shop.observers.iterator().next(), FarmShop.class.getName());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = BM_OBSERVER_METHOD_RESOLUTION, id = "c")
    public <T> void testEventObjectContainsTypeVariablesWhenResolvingFails() {
        eventObjectContainsTypeVariables(new ArrayList<T>());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MULTIPLE_EVENT_QUALIFIERS, id = "a"), @SpecAssertion(section = MULTIPLE_EVENT_QUALIFIERS, id = "b") })
    public void testObserverMethodNotifiedWhenQualifiersMatch() {

        BullTerrier.reset();

        getCurrentManager().getEvent().select(MultiBindingEvent.class, new RoleLiteral("Admin"), new TameAnnotationLiteral()).fire(new MultiBindingEvent());

        assertTrue(BullTerrier.isMultiBindingEventObserved());
        assertTrue(BullTerrier.isSingleBindingEventObserved());
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "dc")
    public void testNonStaticObserverMethodInherited() {
        Egg egg = new Egg();
        getCurrentManager().getEvent().select(Egg.class).fire(egg);
        assertTrue(typeSetMatches(egg.getClassesVisited(), Farmer.class, LazyFarmer.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "di") })
    public void testNonStaticObserverMethodIndirectlyInherited() {
        StockPrice price = new StockPrice();
        getCurrentManager().getEvent().select(StockPrice.class).fire(price);
        assertTrue(typeSetMatches(price.getClassesVisited(), StockWatcher.class, IntermediateStockWatcher.class,
                IndirectStockWatcher.class));
    }

    private <E> void eventObjectContainsTypeVariables(ArrayList<E> eventToFire) {
        getCurrentManager().resolveObserverMethods(eventToFire);
    }

}
