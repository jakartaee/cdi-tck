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
package org.jboss.jsr299.tck.tests.event.observer;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.event.Reception;
import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ObserverTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverTest.class).build();
    }

    @Test(groups = { "events" })
    @SpecAssertions({ @SpecAssertion(section = "10.2", id = "i"), @SpecAssertion(section = "10.5", id = "aa") })
    public void testObserverNotifiedWhenEventTypeAndAllBindingsMatch() {
        Annotation roleBinding = new RoleBinding("Admin");

        // Fire an event that will be delivered to the two above observers
        AnEventType anEvent = new AnEventType();
        getCurrentManager().fireEvent(anEvent, roleBinding);

        assert AnObserver.wasNotified;
        assert AnotherObserver.wasNotified;
        AnObserver.wasNotified = false;
        AnotherObserver.wasNotified = false;

        // Fire an event that will be delivered to only one
        getCurrentManager().fireEvent(anEvent);
        assert AnObserver.wasNotified;
        assert !AnotherObserver.wasNotified;
        AnObserver.wasNotified = false;
        AnotherObserver.wasNotified = false;

        // Also make sure the binding value is considered
        getCurrentManager().fireEvent(anEvent, new RoleBinding("user"));
        assert AnObserver.wasNotified;
        assert !AnotherObserver.wasNotified;
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "11.1.3", id = "b")
    public void testGetBeanOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assert observers.size() == 1;
        ObserverMethod<? super StockPrice> observerMethod = observers.iterator().next();
        assert observerMethod.getBeanClass().equals(StockWatcher.class);
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "11.1.3", id = "c")
    public void testGetObservedTypeOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assert observers.size() == 1;
        ObserverMethod<?> observerMethod = observers.iterator().next();
        assert observerMethod.getObservedType().equals(StockPrice.class);
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "11.1.3", id = "c")
    public void testGetObservedBindingsOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assert observers.size() == 1;
        ObserverMethod<?> observerMethod = observers.iterator().next();
        assert observerMethod.getObservedQualifiers().isEmpty();
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "11.1.3", id = "d")
    public void testGetNotifyOnObserverMethod() {
        Set<ObserverMethod<? super StockPrice>> observers = getCurrentManager().resolveObserverMethods(new StockPrice());
        assert observers.size() == 1;
        assert observers.iterator().next().getReception().equals(Reception.ALWAYS);

        Set<ObserverMethod<? super ConditionalEvent>> conditionalObservers = getCurrentManager().resolveObserverMethods(
                new ConditionalEvent());
        assert !conditionalObservers.isEmpty();
        assert conditionalObservers.iterator().next().getReception().equals(Reception.IF_EXISTS);
    }

}
