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
package org.jboss.cdi.tck.tests.event.eventTypes;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Dan Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class EventTypesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EventTypesTest.class).build();
    }

    @Inject
    Event<Song> songEvent;

    @Inject
    Event<Song[]> songArrayEvent;

    @Inject
    Event<int[]> intArrayEvent;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.1", id = "aa"), @SpecAssertion(section = "10.1", id = "j") })
    public void testEventTypeIsConcreteTypeWithNoTypeVariables() {
        Listener listener = getInstanceByType(Listener.class);
        listener.reset();
        // typical concrete type
        Song s = new Song();
        getInstanceByType(TuneSelect.class).songPlaying(s);
        assert listener.getObjectsFired().size() == 1;
        assert listener.getObjectsFired().get(0) == s;
        songEvent.fire(s);
        // Fire an event via built-in bean is more typical
        // getCurrentManager().fireEvent(s);
        assert listener.getObjectsFired().size() == 2;
        assert listener.getObjectsFired().get(1) == s;
        // anonymous instance
        Broadcast b = new Broadcast() {
        };
        getInstanceByType(TuneSelect.class).broadcastPlaying(b);
        assert listener.getObjectsFired().size() == 3;
        assert listener.getObjectsFired().get(2) == b;
        // boxed primitive
        getCurrentManager().fireEvent(1);
        assert listener.getObjectsFired().size() == 4;
        assert listener.getObjectsFired().get(3).equals(1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "10.1", id = "j") })
    public void testEventTypeIsArray() {

        Listener listener = getInstanceByType(Listener.class);
        listener.reset();

        // array of concrete types
        Song[] songArray = new Song[] { new Song() };
        songArrayEvent.fire(songArray);
        assertEquals(listener.getObjectsFired().size(), 1);
        assertTrue(listener.getObjectsFired().get(0) instanceof Song[]);
        assertEquals(listener.getObjectsFired().get(0), songArray);

        // array of boxed primitives
        Integer[] integerArray = new Integer[] { 0, 1 };
        getCurrentManager().fireEvent(integerArray);
        assertEquals(listener.getObjectsFired().size(), 2);
        assertTrue(listener.getObjectsFired().get(1) instanceof Integer[]);
        assertEquals(listener.getObjectsFired().get(1), integerArray);

        // array of primitives
        int[] intArray = new int[] { 1, 2 };
        intArrayEvent.fire(intArray);
        assertEquals(listener.getObjectsFired().size(), 3);
        assertTrue(listener.getObjectsFired().get(2) instanceof int[]);
        assertEquals(listener.getObjectsFired().get(2), intArray);
    }

    @Test
    @SpecAssertion(section = "10.1", id = "c")
    public void testEventTypeIncludesAllSuperclassesAndInterfacesOfEventObject() {
        // we have to use a dependent-scoped observer here because we it is observing the Object event type
        // and a request-scoped object would get called outside of when the request scope is active in this situation
        EventTypeFamilyObserver observer = getInstanceByType(EventTypeFamilyObserver.class);
        observer.reset();
        getCurrentManager().fireEvent(new ComplexEvent());
        assert observer.getGeneralEventQuantity() == 1;
        assert observer.getAbstractEventQuantity() == 1;
        assert observer.getComplexEventQuantity() == 1;
        assert observer.getObjectEventQuantity() == 1;
        assert observer.getTotalEventsObserved() == 4;
    }
}
