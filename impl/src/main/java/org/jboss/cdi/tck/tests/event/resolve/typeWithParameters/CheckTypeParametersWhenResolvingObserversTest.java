/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.resolve.typeWithParameters;

import static org.jboss.cdi.tck.cdi.Sections.BM_OBSERVER_METHOD_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVERS_ASSIGNABILITY;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHODS;
import static org.jboss.cdi.tck.tests.event.resolve.typeWithParameters.AbstractObserver.buildActionId;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.ObserverMethod;
import jakarta.enterprise.util.TypeLiteral;
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

@SpecVersion(spec = "cdi", version = "2.0")
public class CheckTypeParametersWhenResolvingObserversTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CheckTypeParametersWhenResolvingObserversTest.class).build();
    }

    @Inject
    Event<Box<Integer, String, Random>> event;

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "b"),
            @SpecAssertion(section = BM_OBSERVER_METHOD_RESOLUTION, id = "a") })
    public void testResolvingChecksTypeParameters() {
        verifyObserver(new StringList(), 1, StringListObserver.class);
        verifyObserver(new IntegerList(), 1, IntegerListObserver.class);
        verifyObserver(new CharacterList(), 0);
    }

    @Test
    @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "b")
    public void testParameterizedEventTypeAssignableToRawType() {
        Box<Integer, String, Random> box = new Box<Integer, String, Random>();
        event.fire(box);
        assertTrue(RawTypeObserver.OBSERVED);
        verifyObserver(new RawTypeObserver.BoxWithDifferentTypeParameters(), 1, RawTypeObserver.class);
        verifyObserver(new RawTypeObserver.BoxWithObjectTypeParameters(), 1, RawTypeObserver.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "c"),
            @SpecAssertion(section = OBSERVER_METHODS, id = "ab") })
    public void testObservedEventTypeParameterIsActualType() {
        ActionSequence.reset();
        Foo<String> fooString = new Foo.FooString();
        verifyObserver(fooString, 1, FooObserver.class);
        getCurrentManager().getEvent().select(new TypeLiteral<Foo<String>>() {
        }).fire(fooString);
        verifyEvent(FooObserver.SEQUENCE, 1, buildActionId(FooObserver.class, fooString));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "d") })
    public void testObservedEventTypeParameterIsActualTypeNested() {
        ActionSequence.reset();
        Foo<List<String>> fooStringList = new Foo.FooStringList();
        verifyObserver(fooStringList, 1, FooObserver.class);
        getCurrentManager().getEvent().select(new TypeLiteral<Foo<List<String>>>() {
        }).fire(fooStringList);
        verifyEvent(FooObserver.SEQUENCE_NESTED, 1, buildActionId(FooObserver.class, fooStringList));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "e") })
    public void testObservedEventTypeParameterIsWildcard() {

        ActionSequence.reset();

        Qux<String> quxString = new Qux.QuxString();
        Qux<List<String>> quxStringList = new Qux.QuxStringList();
        Qux<Number> quxNumber = new Qux.QuxNumber();

        verifyObserver(quxString, 1, WildcardObserver.class);
        verifyObserver(quxNumber, 2, WildcardObserver.class);
        verifyObserver(quxStringList, 2, WildcardObserver.class);

        getCurrentManager().getEvent().select(new TypeLiteral<Qux<String>>() {
        }).fire(quxString);
        getCurrentManager().getEvent().select(new TypeLiteral<Qux<List<String>>>() {
        }).fire(quxStringList);
        getCurrentManager().getEvent().select(new TypeLiteral<Qux<Number>>() {
        }).fire(quxNumber);

        verifyEvent(WildcardObserver.SEQUENCE, 3, buildActionId(WildcardObserver.class, quxString),
                buildActionId(WildcardObserver.class, quxStringList), buildActionId(WildcardObserver.class, quxNumber));
        verifyEvent(WildcardObserver.SEQUENCE_LOWER, 1, buildActionId(WildcardObserver.class, quxNumber));
        verifyEvent(WildcardObserver.SEQUENCE_UPPER, 1, buildActionId(WildcardObserver.class, quxStringList));
    }

    @Test
    @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "f")
    public void testObservedEventTypeParameterIsTypeVariable() {

        ActionSequence.reset();

        Duck<String> duckString = new Duck.DuckString();
        Duck<Integer> duckInteger = new Duck.DuckInteger();

        verifyObserver(duckString, 1, TypeVariableObserver.class);
        verifyObserver(duckInteger, 2, TypeVariableObserver.class);

        getCurrentManager().getEvent().select(new TypeLiteral<Duck<String>>() {
        }).fire(duckString);
        getCurrentManager().getEvent().select(new TypeLiteral<Duck<Integer>>() {
        }).fire(duckInteger);

        verifyEvent(TypeVariableObserver.SEQUENCE_TYPE_VAR, 2, buildActionId(TypeVariableObserver.class, duckString),
                buildActionId(TypeVariableObserver.class, duckInteger));
        verifyEvent(TypeVariableObserver.SEQUENCE_TYPE_VAR_UPPER, 1, buildActionId(TypeVariableObserver.class, duckInteger));

    }

    @Test
    @SpecAssertion(section = OBSERVERS_ASSIGNABILITY, id = "a")
    public void testEventTypeAssignableToATypeVariable() {

        ActionSequence.reset();

        Bar bar = new Bar();
        Baz baz = new Baz();

        verifyObserver(bar, 1, TypeVariableObserver.class);
        verifyObserver(baz, 1, TypeVariableObserver.class);

        getCurrentManager().getEvent().select(Bar.class).fire(new Bar());
        getCurrentManager().getEvent().select(Baz.class).fire(new Baz());
        getCurrentManager().getEvent().select(StringList.class).fire(new StringList());

        verifyEvent(TypeVariableObserver.SEQUENCE_UPPER, 2, buildActionId(TypeVariableObserver.class, bar),
                buildActionId(TypeVariableObserver.class, baz));
    }

    /**
     * Quick and dirty test for various event type resolution use cases.
     */
    @Test
    public void testEventTypeResolution() {
        int expectedMatches = 5;
        Dog<?, ?> dogStringNumber = new Dog.DogStringNumber();
        verifyObserver(dogStringNumber, expectedMatches, DogObserver.class);
        getCurrentManager().getEvent().select(new TypeLiteral<Dog<?, ?>>() {
        }).fire(dogStringNumber);
        verifyEvent(DogObserver.SEQUENCE, expectedMatches);

    }

    public static class CharacterList extends ArrayList<Character> {
        private static final long serialVersionUID = 1L;
    }

    public static class StringList extends ArrayList<String> {
        private static final long serialVersionUID = 1L;
    }

    public static class IntegerList extends ArrayList<Integer> {
        private static final long serialVersionUID = 1L;
    }

    @Dependent
    public static class StringListObserver {
        public boolean wasNotified = false;

        public void observer(@Observes ArrayList<String> event) {
            wasNotified = true;
        }
    }

    @Dependent
    public static class IntegerListObserver {
        public boolean wasNotified = false;

        public void observer(@Observes ArrayList<Integer> event) {
            wasNotified = true;
        }
    }

    private void verifyObserver(Object event, int expectedNumberOfObservers, Class<?>... expectedObserverTypes) {

        if (expectedNumberOfObservers < expectedObserverTypes.length) {
            throw new IllegalArgumentException("Invalid expected arguments");
        }

        Set<ObserverMethod<? super Object>> observers = getCurrentManager().resolveObserverMethods(event);
        assertEquals(observers.size(), expectedNumberOfObservers);

        List<Class<?>> observerTypes = Arrays.asList(expectedObserverTypes);
        for (ObserverMethod<? super Object> observer : observers) {
            assertTrue(observerTypes.contains(observer.getBeanClass()));
        }
    }

    /**
     * @param sequenceName
     * @param expectedSequenceSize
     * @param actions Sequence data must contain all specified actions
     */
    private void verifyEvent(String sequenceName, int expectedSequenceSize, String... actions) {

        List<String> sequenceData = ActionSequence.getSequenceData(sequenceName);
        assertNotNull(sequenceData);
        assertEquals(sequenceData.size(), expectedSequenceSize);

        for (String action : actions) {
            assertTrue(sequenceData.contains(action));
        }
    }
}
