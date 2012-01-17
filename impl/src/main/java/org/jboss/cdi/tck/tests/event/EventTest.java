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

import java.util.ArrayList;

import javax.enterprise.context.spi.Context;
import javax.enterprise.event.TransactionPhase;

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
 * TODO This test was temporarily marked as integration one because of problems with <b>arquillian-weld-ee-embedded-1.1</b> in
 * {@link #testStaticObserverMethodInvoked()}.
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
        return new WebArchiveBuilder().withTestClassPackage(EventTest.class).withBeansXml("beans.xml").build();
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.4.2", id = "i"), @SpecAssertion(section = "5.5.6", id = "c"),
            @SpecAssertion(section = "2.3.5", id = "ca"), @SpecAssertion(section = "3.11", id = "a") })
    public void testObserverMethodReceivesInjectionsOnNonObservesParameters() {
        getCurrentManager().fireEvent("validate injected parameters");
    }

    /**
     * FIXME the spec doesn't follow this exactly because technically it isn't supposed to use the bean resolution algorithm to
     * obtain the instance, which it does.
     */
    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.4", id = "c"), @SpecAssertion(section = "5.5.6", id = "a") })
    public void testStaticObserverMethodInvoked() {
        Context requestContext = getCurrentConfiguration().getContexts().getRequestContext();
        try {
            getCurrentConfiguration().getContexts().setInactive(requestContext);
            StaticObserver.reset();
            getCurrentManager().fireEvent(new Delivery());
            assert StaticObserver.isDeliveryReceived();
            StaticObserver.reset();
        } finally {
            getCurrentConfiguration().getContexts().setActive(requestContext);
        }
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "4.3", id = "cc"), @SpecAssertion(section = "5.5.6", id = "baa") })
    public void testObserverCalledOnMostSpecializedInstance() {
        Shop.observers.clear();
        getCurrentManager().fireEvent(new Delivery());
        assert Shop.observers.size() == 1;
        assert Shop.observers.iterator().next().equals(FarmShop.class.getName());
    }

    @Test(groups = { EVENTS }, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.11", id = "c")
    public <T> void testEventObjectContainsTypeVariablesWhenResolvingFails() {
        eventObjectContainsTypeVariables(new ArrayList<T>());
    }

    private <E> void eventObjectContainsTypeVariables(ArrayList<E> eventToFire) {
        getCurrentManager().resolveObserverMethods(eventToFire);
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.2.3", id = "b"), @SpecAssertion(section = "10.2.3", id = "c") })
    public void testObserverMethodNotifiedWhenBindingsMatch() {
        getCurrentManager().fireEvent(new MultiBindingEvent(), new RoleBinding("Admin"), new TameAnnotationLiteral());
        assert BullTerrier.isMultiBindingEventObserved();
        assert BullTerrier.isSingleBindingEventObserved();
    }

    /**
     * By default, Java implementation reuse is assumed. In this case, the producer, disposal and observer methods of the first
     * bean are not inherited by the second bean.
     * 
     * @throws Exception
     */
    @Test(groups = { EVENTS, INHERITANCE })
    @SpecAssertion(section = "4.2", id = "dc")
    public void testNonStaticObserverMethodInherited() {
        Egg egg = new Egg();
        getCurrentManager().fireEvent(egg);
        assert typeSetMatches(egg.getClassesVisited(), Farmer.class, LazyFarmer.class);
    }

    @Test(groups = { EVENTS, INHERITANCE })
    @SpecAssertions({ @SpecAssertion(section = "4.2", id = "di"), @SpecAssertion(section = "11.1.3", id = "f") })
    public void testNonStaticObserverMethodIndirectlyInherited() {
        StockPrice price = new StockPrice();
        getCurrentManager().fireEvent(price);
        assert typeSetMatches(price.getClassesVisited(), StockWatcher.class, IntermediateStockWatcher.class,
                IndirectStockWatcher.class);
    }

    @Test(groups = { EVENTS })
    @SpecAssertion(section = "11.1.3", id = "e")
    public void testGetTransactionPhaseOnObserverMethod() {
        assert getCurrentManager().resolveObserverMethods(new StockPrice()).iterator().next().getTransactionPhase()
                .equals(TransactionPhase.IN_PROGRESS);
        assert getCurrentManager().resolveObserverMethods(new DisobedientDog()).iterator().next().getTransactionPhase()
                .equals(TransactionPhase.BEFORE_COMPLETION);
        assert getCurrentManager().resolveObserverMethods(new ShowDog()).iterator().next().getTransactionPhase()
                .equals(TransactionPhase.AFTER_COMPLETION);
        assert getCurrentManager().resolveObserverMethods(new SmallDog()).iterator().next().getTransactionPhase()
                .equals(TransactionPhase.AFTER_FAILURE);
        assert getCurrentManager().resolveObserverMethods(new LargeDog()).iterator().next().getTransactionPhase()
                .equals(TransactionPhase.AFTER_SUCCESS);
    }

    @Test(groups = { EVENTS })
    @SpecAssertion(section = "11.1.3", id = "ga")
    public void testInstanceOfBeanForEveryEnabledObserverMethod() {
        assert !getCurrentManager().resolveObserverMethods(new StockPrice()).isEmpty();
        assert !getCurrentManager().resolveObserverMethods(new DisobedientDog()).isEmpty();
        assert !getCurrentManager().resolveObserverMethods(new ShowDog()).isEmpty();
        assert !getCurrentManager().resolveObserverMethods(new SmallDog()).isEmpty();
        assert !getCurrentManager().resolveObserverMethods(new LargeDog()).isEmpty();
    }
}
