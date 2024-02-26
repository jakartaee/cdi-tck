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
package org.jboss.cdi.tck.tests.event.observer.priority;

import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_ORDERING;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.ObserverMethod;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Mark Paluch
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EventObserverOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(Sunrise.class).build();
    }

    @Inject
    private Event<Sunset> sunset;

    @Inject
    private Event<Sunrise> sunrise;

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_ORDERING, id = "b") })
    public void testFireEventLowerPriorityBeforeDefaultPriority() {

        ActionSequence.reset();
        sunrise.fire(new Sunrise());

        assertEquals(ActionSequence.getSequenceSize(), 3);

        // APPLICATION + 499
        assertTrue(ActionSequence.getSequence().beginsWith(SunriseObservers.AsianObserver.class.getName()));

        // DEFAULT
        ActionSequence.assertSequenceDataContainsAll(SunriseObservers.GermanObserver.class.getName(),
                SunriseObservers.ItalianObserver.class.getName());

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_ORDERING, id = "b") })
    public void testResolveObserversLowerPriorityBeforeDefaultPriority() {

        Set<ObserverMethod<? super Sunrise>> observerMethods = getCurrentManager().resolveObserverMethods(new Sunrise());

        assertEquals(3, observerMethods.size());
        assertEquals(observerMethods.iterator().next().getBeanClass(), SunriseObservers.AsianObserver.class);

        List<Class<?>> classes = observerMethods.stream().map(ObserverMethod::getBeanClass).collect(Collectors.toList());

        assertTrue(classes.contains(SunriseObservers.GermanObserver.class));
        assertTrue(classes.contains(SunriseObservers.ItalianObserver.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_ORDERING, id = "b") })
    public void testFireEventToMultipleObserversWithPriority() {

        ActionSequence.reset();
        sunset.fire(new Sunset());

        assertEquals(ActionSequence.getSequenceSize(), 3);

        // 2599, 2600, 2700
        assertTrue(ActionSequence.getSequence().beginsWith(SunsetObservers.AsianObserver.class.getName(),
                SunsetObservers.EuropeanObserver.class.getName(), SunsetObservers.AmericanObserver.class.getName()));

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_ORDERING, id = "b") })
    public void testPrioritizedEventSubclass() {

        ActionSequence.reset();
        getCurrentManager().getEvent().select(Moonrise.class).fire(new Moonrise());

        assertEquals(ActionSequence.getSequenceSize(), 4);

        // APPLICATION, DEFAULT, APPLICATION + 900, , APPLICATION + 950

        ActionSequence.assertSequenceDataEquals(MoonObservers.Observer1.class.getName(),
                MoonObservers.Observer2.class.getName(), MoonObservers.Observer3.class.getName(),
                MoonObservers.Observer4.class.getName());

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_ORDERING, id = "b") })
    public void testPrioritizedEventBaseclass() {

        ActionSequence.reset();
        getCurrentManager().getEvent().select(MoonActivity.class).fire(new MoonActivity());

        assertEquals(ActionSequence.getSequenceSize(), 2);

        // APPLICATION, APPLICATION + 900

        ActionSequence.assertSequenceDataEquals(MoonObservers.Observer1.class.getName(),
                MoonObservers.Observer3.class.getName());

    }

}
