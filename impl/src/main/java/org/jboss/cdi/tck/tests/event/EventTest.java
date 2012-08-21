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

import static org.jboss.cdi.tck.TestGroups.EVENTS;
import static org.jboss.cdi.tck.TestGroups.INHERITANCE;
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import javax.enterprise.context.spi.Context;

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
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class EventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EventTest.class).build();
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.4.2", id = "i"), @SpecAssertion(section = "5.5.6", id = "c"),
            @SpecAssertion(section = "2.3.5", id = "ca"), @SpecAssertion(section = "3.11", id = "a") })
    public void testObserverMethodParameterInjectionPoints() {
        TerrierObserver.reset();
        getCurrentManager().fireEvent(new BullTerrier());
        assertTrue(TerrierObserver.eventObserved);
        assertTrue(TerrierObserver.parametersInjected);
    }

    /**
     * This test was temporarily marked as integration one because of problems with arquillian-weld-ee-embedded-1.1 container
     * adapter.
     */
    @Test(groups = { EVENTS, INTEGRATION })
    @SpecAssertions({ @SpecAssertion(section = "10.4", id = "c"), @SpecAssertion(section = "5.5.6", id = "a") })
    public void testStaticObserverMethodInvoked() {

        Context requestContext = getCurrentConfiguration().getContexts().getRequestContext();

        try {
            // Deactivate request context so that we're sure the contextual instance is not obtained
            getCurrentConfiguration().getContexts().setInactive(requestContext);

            StaticObserver.reset();
            getCurrentManager().fireEvent(new Delivery());

            assertTrue(StaticObserver.isDeliveryReceived());

        } finally {
            getCurrentConfiguration().getContexts().setActive(requestContext);
        }
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "4.3", id = "cc"), @SpecAssertion(section = "5.5.6", id = "baa") })
    public void testObserverCalledOnSpecializedBeanOnly() {
        Shop.observers.clear();
        getCurrentManager().fireEvent(new Delivery());
        // FarmShop specializes Shop
        assertEquals(Shop.observers.size(), 1);
        assertEquals(Shop.observers.iterator().next(), FarmShop.class.getName());
    }

    @Test(groups = { EVENTS }, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.11", id = "c")
    public <T> void testEventObjectContainsTypeVariablesWhenResolvingFails() {
        eventObjectContainsTypeVariables(new ArrayList<T>());
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.2.3", id = "a"), @SpecAssertion(section = "10.2.3", id = "b") })
    public void testObserverMethodNotifiedWhenQualifiersMatch() {

        BullTerrier.reset();

        getCurrentManager().fireEvent(new MultiBindingEvent(), new RoleLiteral("Admin"), new TameAnnotationLiteral());

        assertTrue(BullTerrier.isMultiBindingEventObserved());
        assertTrue(BullTerrier.isSingleBindingEventObserved());
    }

    @Test(groups = { EVENTS, INHERITANCE })
    @SpecAssertion(section = "4.2", id = "dc")
    public void testNonStaticObserverMethodInherited() {
        Egg egg = new Egg();
        getCurrentManager().fireEvent(egg);
        assertTrue(typeSetMatches(egg.getClassesVisited(), Farmer.class, LazyFarmer.class));
    }

    @Test(groups = { EVENTS, INHERITANCE })
    @SpecAssertions({ @SpecAssertion(section = "4.2", id = "di") })
    public void testNonStaticObserverMethodIndirectlyInherited() {
        StockPrice price = new StockPrice();
        getCurrentManager().fireEvent(price);
        assertTrue(typeSetMatches(price.getClassesVisited(), StockWatcher.class, IntermediateStockWatcher.class,
                IndirectStockWatcher.class));
    }

    private <E> void eventObjectContainsTypeVariables(ArrayList<E> eventToFire) {
        getCurrentManager().resolveObserverMethods(eventToFire);
    }

}
