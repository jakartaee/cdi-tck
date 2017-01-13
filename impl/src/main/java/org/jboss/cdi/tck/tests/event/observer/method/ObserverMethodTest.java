/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.event.observer.method;

import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class ObserverMethodTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverMethodTest.class).build();
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD, id = "b")
    public void testGetBeanClassOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assertEquals(observers.size(), 1);
        ObserverMethod<? super StockPrice> observerMethod = observers.iterator().next();
        assertEquals(observerMethod.getBeanClass(), StockWatcher.class);
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD, id = "c")
    public void testGetObservedTypeOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assertEquals(observers.size(), 1);
        ObserverMethod<?> observerMethod = observers.iterator().next();
        assertEquals(observerMethod.getObservedType(), StockPrice.class);
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD, id = "c")
    public void testGetObservedQualifiersOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assertEquals(observers.size(), 1);
        ObserverMethod<?> observerMethod = observers.iterator().next();
        assertTrue(observerMethod.getObservedQualifiers().isEmpty());
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD, id = "d")
    public void testGetNotifyOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assertEquals(observers.size(), 1);
        assertEquals(observers.iterator().next().getReception(), Reception.ALWAYS);

        Set<ObserverMethod<? super ConditionalEvent>> conditionalObservers = getCurrentManager().resolveObserverMethods(
                new ConditionalEvent());
        assertFalse(conditionalObservers.isEmpty());
        assertEquals(conditionalObservers.iterator().next().getReception(), Reception.IF_EXISTS);
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD, id = "e")
    public void testGetTransactionPhaseOnObserverMethod() {

        assertEquals(getCurrentManager().resolveObserverMethods(new StockPrice()).iterator().next().getTransactionPhase(),
                TransactionPhase.IN_PROGRESS);
        assertEquals(getCurrentManager().resolveObserverMethods(new DisobedientDog()).iterator().next().getTransactionPhase(),
                TransactionPhase.BEFORE_COMPLETION);
        assertEquals(getCurrentManager().resolveObserverMethods(new ShowDog()).iterator().next().getTransactionPhase(),
                TransactionPhase.AFTER_COMPLETION);
        assertEquals(getCurrentManager().resolveObserverMethods(new SmallDog()).iterator().next().getTransactionPhase(),
                TransactionPhase.AFTER_FAILURE);
        assertEquals(getCurrentManager().resolveObserverMethods(new LargeDog()).iterator().next().getTransactionPhase(),
                TransactionPhase.AFTER_SUCCESS);
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD, id = "ga")
    public void testInstanceOfBeanForEveryEnabledObserverMethod() {
        assertFalse(getCurrentManager().resolveObserverMethods(new StockPrice()).isEmpty());
        assertFalse(getCurrentManager().resolveObserverMethods(new DisobedientDog()).isEmpty());
        assertFalse(getCurrentManager().resolveObserverMethods(new ShowDog()).isEmpty());
        assertFalse(getCurrentManager().resolveObserverMethods(new SmallDog()).isEmpty());
        assertFalse(getCurrentManager().resolveObserverMethods(new LargeDog()).isEmpty());
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD, id = "f")
    public void testNotifyOnObserverMethod() {
        Integer event = Integer.valueOf(1);
        Set<ObserverMethod<? super Integer>> observerMethods = getCurrentManager().resolveObserverMethods(event, new AnnotationLiteral<Number>() {
        });
        assertEquals(observerMethods.size(), 1);
        observerMethods.iterator().next().notify(event);
        assertTrue(IntegerObserver.wasNotified);
    }

}
