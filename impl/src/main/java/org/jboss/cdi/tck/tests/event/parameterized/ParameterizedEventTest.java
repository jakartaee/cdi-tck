/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.parameterized;

import static org.jboss.cdi.tck.cdi.Sections.EVENT;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVERS_ASSIGNABILITY;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVES;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test that verifies that the container uses the runtime type of the event object as the event type. If the event type contains
 * an unresolved type variable the selected type is used to resolve it.
 *
 *
 * This test was originally part of the Weld test suite.
 *
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 *
 * @see <a >WELD-1272</a>
 * @see <a>CDI-256</a>
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ParameterizedEventTest extends AbstractTest {

    @Inject
    private Event<Object> event;

    @Inject
    private Event<Foo<List<Integer>>> integerListFooEvent;

    @Inject
    private Event<Bar<List<Integer>>> integerListBarEvent;

    @Inject
    private EventObserver observer;

    @Inject
    private IntegerListObserver integerObserver;

    @Inject
    private StringListObserver stringObserver;

    @Inject
    Event<Foo<? extends Number>> fooEvent;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ParameterizedEventTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "edb"), @SpecAssertion(section = OBSERVES, id = "a") })
    public void testSelectedEventTypeUsedForResolvingEventTypeArguments() {
        reset();
        // Event types: Bar<T>, Foo<T>, Fooable<T>
        // T resolved to List<Integer>
        integerListBarEvent.fire(new Bar<List<Integer>>());

        assertTrue(observer.isIntegerListFooableObserved());
        assertTrue(observer.isIntegerListFooObserved());
        assertTrue(observer.isIntegerListBarObserved());
        assertFalse(observer.isBazObserved());
        assertFalse(observer.isStringListFooableObserved());

        assertTrue(integerObserver.isFooableObserved());
        assertTrue(integerObserver.isFooObserved());
        assertTrue(integerObserver.isBarObserved());

        assertFalse(stringObserver.isFooableObserved());
        assertFalse(stringObserver.isFooObserved());
        assertFalse(stringObserver.isBarObserved());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "edb"), @SpecAssertion(section = OBSERVES, id = "a") })
    public void testSelectedEventTypeUsedForResolvingEventTypeArguments2() {
        reset();
        @SuppressWarnings("serial")
        Event<Foo<List<Integer>>> selectedEvent = event.select(new TypeLiteral<Foo<List<Integer>>>() {
        });
        // Event types: Foo<T>, Fooable<T>
        // T resolved to List<Integer>
        selectedEvent.fire(new Foo<List<Integer>>());

        assertTrue(observer.isIntegerListFooableObserved());
        assertTrue(observer.isIntegerListFooObserved());
        assertFalse(observer.isIntegerListBarObserved());
        assertFalse(observer.isBazObserved());
        assertFalse(observer.isStringListFooableObserved());

        assertTrue(integerObserver.isFooableObserved());
        assertTrue(integerObserver.isFooObserved());
        assertFalse(integerObserver.isBarObserved());

        assertFalse(stringObserver.isFooableObserved());
        assertFalse(stringObserver.isFooObserved());
        assertFalse(stringObserver.isBarObserved());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "edb"), @SpecAssertion(section = OBSERVES, id = "a") })
    public void testSelectedEventTypeCombinedWithEventObjectRuntimeTypeForResolvingEventTypeArguments() {
        reset();
        @SuppressWarnings("serial")
        Event<Foo<List<Integer>>> selectedEvent = event.select(new TypeLiteral<Foo<List<Integer>>>() {
        });
        selectedEvent.fire(new Bar<List<Integer>>());

        assertTrue(observer.isIntegerListFooableObserved());
        assertTrue(observer.isIntegerListFooObserved());
        assertTrue(observer.isIntegerListBarObserved());
        assertFalse(observer.isBazObserved());
        assertFalse(observer.isStringListFooableObserved());

        assertTrue(integerObserver.isFooableObserved());
        assertTrue(integerObserver.isFooObserved());
        assertTrue(integerObserver.isBarObserved());

        assertFalse(stringObserver.isFooableObserved());
        assertFalse(stringObserver.isFooObserved());
        assertFalse(stringObserver.isBarObserved());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "edb") })
    public void testSelectedEventTypeCombinedWithEventObjectRuntimeTypeForResolvingEventTypeArguments2() {
        reset();
        @SuppressWarnings("serial")
        Event<List<Character>> selectedEvent = event.select(new TypeLiteral<List<Character>>() {
        });
        selectedEvent.fire(new ArrayList<Character>());

        assertTrue(observer.isCharacterListObserved());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "d") })
    public void testEventObjectTypeUsed() {
        reset();

        // Event types: Baz, Bar<List<Integer>>, Foo<List<Integer>>, Fooable<List<Integer>>
        integerListBarEvent.fire(new Baz());

        assertTrue(observer.isIntegerListFooableObserved());
        assertTrue(observer.isIntegerListFooObserved());
        assertTrue(observer.isIntegerListBarObserved());
        assertTrue(observer.isBazObserved());
        assertFalse(observer.isStringListFooableObserved());

        assertTrue(integerObserver.isFooableObserved());
        assertTrue(integerObserver.isFooObserved());
        assertTrue(integerObserver.isBarObserved());

        assertFalse(stringObserver.isFooableObserved());
        assertFalse(stringObserver.isFooObserved());
        assertFalse(stringObserver.isBarObserved());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "f") })
    public void testUnresolvedTypeVariableDetected1() {
        try {
            // Blah B2 cannot be resolved
            integerListFooEvent.fire(new Blah<List<Integer>, Integer>());
            Assert.fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "f") })
    @SuppressWarnings("serial")
    public <T> void testUnresolvedTypeVariableDetected2() {
        try {
            // T cannot be resolved
            event.select(new TypeLiteral<Map<Exception, T>>() {
            }).fire(new HashMap<Exception, T>());
            Assert.fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "f") })
    @SuppressWarnings("serial")
    public <T> void testUnresolvedTypeVariableDetected3() {
        try {
            event.select(new TypeLiteral<ArrayList<List<List<List<T>>>>>() {
            }).fire(new ArrayList<List<List<List<T>>>>());
            Assert.fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "e") })
    public void testWildcardIsResolvable() {
        reset();
        fooEvent.fire(new Bar<Integer>());
        assertTrue(observer.isIntegerFooObserved());
    }

    private void reset() {
        observer.reset();
        integerObserver.reset();
        stringObserver.reset();
    }
}
