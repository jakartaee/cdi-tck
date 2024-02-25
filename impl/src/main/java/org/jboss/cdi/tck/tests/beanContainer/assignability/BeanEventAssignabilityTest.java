/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.beanContainer.assignability;

import static org.jboss.cdi.tck.cdi.Sections.BM_BEAN_EVENT_ASSIGNABILITY;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Andrew Rouse
 */
@SpecVersion(spec = "cdi", version = "4.1")
public class BeanEventAssignabilityTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanEventAssignabilityTest.class).build();
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "aa")
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "af")
    public void testBeanMatching() {
        Set<Type> beanTypes = Set.of(MyBean.class, MyBeanInterface.class, Object.class);

        // Basic checks with qualifiers
        assertTrue(getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), MyBean.class, Set.of()),
                "Bean did not match its own type");
        assertFalse(
                getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), MyBean.class,
                        Set.of(Qualifier1.Literal.INSTANCE)),
                "Bean matched despite not having required qualifier");
        assertTrue(
                getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(Qualifier1.Literal.INSTANCE), MyBean.class,
                        Set.of(Qualifier1.Literal.INSTANCE)),
                "Bean did not match despite having required qualifier");
        assertTrue(
                getCurrentBeanContainer().isMatchingBean(beanTypes,
                        Set.of(Qualifier1.Literal.INSTANCE, Qualifier2.Literal.INSTANCE), MyBean.class,
                        Set.of(Qualifier1.Literal.INSTANCE)),
                "Bean did not match despite having a superset of the required qualifiers");

        // Only bean types passed in should be considered
        Set<Type> reducedBeanTypes = Set.of(MyBean.class);
        assertTrue(getCurrentBeanContainer().isMatchingBean(reducedBeanTypes, Set.of(), MyBean.class, Set.of()),
                "Bean did not match its own type");
        assertFalse(getCurrentBeanContainer().isMatchingBean(reducedBeanTypes, Set.of(), MyBeanInterface.class, Set.of()),
                "Bean matched MyBeanInterface despite it not being in bean types");
        assertTrue(getCurrentBeanContainer().isMatchingBean(reducedBeanTypes, Set.of(), Object.class, Set.of()),
                "Bean did not match when Object requested");

        // Qualifier annotations on bean type classes should not be considered
        assertTrue(
                getCurrentBeanContainer().isMatchingBean(Set.of(MyQualifiedBean.class), Set.of(), MyQualifiedBean.class,
                        Set.of()),
                "MyQualifiedBean should match, qualifier on bean class should be ignored");
        assertFalse(
                getCurrentBeanContainer().isMatchingBean(Set.of(MyQualifiedBean.class), Set.of(), MyQualifiedBean.class,
                        Set.of(Qualifier1.Literal.INSTANCE)),
                "MyQualifiedBean should not match, qualifier on bean class should be ignored");
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "ab")
    public void testBeanMatchingDefaultQualifiers() {
        Set<Type> beanTypes = Set.of(MyBean.class, MyBeanInterface.class, Object.class);

        assertTrue(
                getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(Default.Literal.INSTANCE), MyBean.class, Set.of()),
                "Bean with @Default should match when no qualifiers required");

        assertTrue(
                getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), MyBean.class, Set.of(Default.Literal.INSTANCE)),
                "Bean with no qualifiers should match when @Default required");

        assertTrue(getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(Any.Literal.INSTANCE), MyBean.class, Set.of()),
                "Bean with explicit @Any should match when no qualifiers required");

        assertTrue(getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(NamedLiteral.of("foo")), MyBean.class, Set.of()),
                "Bean with @Named should match when no qualifiers required");

        assertFalse(
                getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(Qualifier1.Literal.INSTANCE), MyBean.class,
                        Set.of()),
                "Bean with @Qualifier1 should not match when no qualifiers required (@Default implied required)");

        assertTrue(getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), MyBean.class, Set.of(Any.Literal.INSTANCE)),
                "Bean with no qualifiers should match when @Any required");

        assertTrue(
                getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(Qualifier1.Literal.INSTANCE), MyBean.class,
                        Set.of(Any.Literal.INSTANCE)),
                "Bean with @Qualifier1 should match when @Any required");
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "ac")
    public void testBeanMatchingNullException() {

        Set<Type> beanTypes = Set.of(MyBean.class, MyBeanInterface.class, Object.class);

        assertThrows("Null bean type", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingBean(null, Set.of(), MyBean.class, Set.of()));
        assertThrows("Null bean qualifiers", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingBean(beanTypes, null, MyBean.class, Set.of()));
        assertThrows("Null required type", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), null, Set.of()));
        assertThrows("Null required qualifiers", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), MyBean.class, null));
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "ad")
    public void testBeanMatchingNonQualifiersException() {

        Set<Type> beanTypes = Set.of(MyBean.class, MyBeanInterface.class, Object.class);

        assertThrows("beanQualifiers annotation not a qualifier", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingBean(beanTypes,
                        Set.of(Qualifier1.Literal.INSTANCE, NonQualifier.Literal.INSTANCE), MyBean.class, Set.of()));

        assertThrows("requiredQualifiers annotation not a qualifier", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), MyBean.class,
                        Set.of(Qualifier1.Literal.INSTANCE, NonQualifier.Literal.INSTANCE)));
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "ae")
    public void testNonLegalBeanTypesIgnored() {
        // A parameterized type that contains a wildcard type parameter is not a legal bean type.
        TypeLiteral<List<?>> listOfWildcard = new TypeLiteral<>() {
        };
        Set<Type> beanTypes = Set.of(MyBean.class, MyBeanInterface.class, listOfWildcard.getType(), Object.class);

        assertTrue(getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), MyBean.class, Set.of()),
                "Non-legal bean type should be ignored, allowing match");

        assertFalse(getCurrentBeanContainer().isMatchingBean(beanTypes, Set.of(), listOfWildcard.getType(), Set.of()),
                "Non-legal bean type should be ignored, preventing match");
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "ba")
    public void testEventMatching() {
        assertTrue(getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), MyEvent.class, Set.of()),
                "Event did not match its own type");

        assertFalse(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), MyEvent.class,
                        Set.of(Qualifier1.Literal.INSTANCE)),
                "Event matched despite not having required qualifier");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(Qualifier1.Literal.INSTANCE), MyEvent.class,
                        Set.of(Qualifier1.Literal.INSTANCE)),
                "Event did not match despite having required qualifier");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class,
                        Set.of(Qualifier1.Literal.INSTANCE, Qualifier2.Literal.INSTANCE), MyEvent.class,
                        Set.of(Qualifier1.Literal.INSTANCE)),
                "Event did not match despite having a superset of the required qualifiers");

        assertTrue(getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), MyEventInterface.class, Set.of()),
                "Event should match when a supertype is required");

        assertTrue(getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), Object.class, Set.of()),
                "Event should match when Object is required");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(Default.Literal.INSTANCE), MyEvent.class,
                        Set.of()),
                "Event with @Default should match when no qualifiers required");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(Any.Literal.INSTANCE), MyEvent.class, Set.of()),
                "Event with explicit @Any should match when no qualifiers required");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(Qualifier1.Literal.INSTANCE), MyEvent.class,
                        Set.of()),
                "Event with @Qualifier1 should match when no qualifiers required");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(NamedLiteral.of("foo")), MyEvent.class,
                        Set.of()),
                "Event with @Named should match when no qualifiers required");

    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "bf")
    public void testEventMatchingDefaultQualifier() {
        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), MyEvent.class,
                        Set.of(Default.Literal.INSTANCE)),
                "Event with no qualifiers should match when @Default required");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(Default.Literal.INSTANCE), MyEvent.class,
                        Set.of(Default.Literal.INSTANCE)),
                "Event with @Default should match when @Default required");

        assertFalse(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(Qualifier1.Literal.INSTANCE), MyEvent.class,
                        Set.of(Default.Literal.INSTANCE)),
                "Event with @Qualifier1 should not match when @Default required");
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "ba")
    public void testEventMatchingParameterized() {
        TypeLiteral<List<String>> listOfString = new TypeLiteral<>() {
        };
        TypeLiteral<List<?>> listOfWildcard = new TypeLiteral<>() {
        };
        TypeLiteral<List<Integer>> listOfInteger = new TypeLiteral<>() {
        };

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(listOfString.getType(), Set.of(), listOfString.getType(), Set.of()),
                "Event with Parameterized type should match when same Parameterized type is requested");
        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(listOfString.getType(), Set.of(), listOfWildcard.getType(), Set.of()),
                "Event with Parameterized type should match when Parameterized type with wildcard is requested");
        assertFalse(
                getCurrentBeanContainer().isMatchingEvent(listOfString.getType(), Set.of(), listOfInteger.getType(), Set.of()),
                "Event with Parameterized type should not match when a different Parameterized type is requested");
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "bb")
    public void testEventMatchingAnyQualifier() {
        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), MyEvent.class, Set.of(Any.Literal.INSTANCE)),
                "Event with no qualifiers should match when @Any required");

        assertTrue(
                getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(Qualifier1.Literal.INSTANCE), MyEvent.class,
                        Set.of(Any.Literal.INSTANCE)),
                "Event with @Qualifier1 should match when @Any required");
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "bc")
    public void testEventMatchingNullException() {
        assertThrows("Null event type", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingEvent(null, Set.of(), MyBean.class, Set.of()));
        assertThrows("Null event qualifiers", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingEvent(MyEvent.class, null, MyEvent.class, Set.of()));
        assertThrows("Null required type", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), null, Set.of()));
        assertThrows("Null required qualifiers", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), MyEvent.class, null));
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "bd")
    public <X> void testEventMatchingTypeVarException() {
        TypeLiteral<List<X>> varEventType = new TypeLiteral<>() {
        };

        assertThrows("Type variable in event type", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingEvent(varEventType.getType(), Set.of(), MyBean.class, Set.of()));
    }

    @Test
    @SpecAssertion(section = BM_BEAN_EVENT_ASSIGNABILITY, id = "be")
    public void testEventMatchingNonQualifiersException() {
        assertThrows("A specifiedQualifiers annotation not a qualifier", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingEvent(MyEvent.class,
                        Set.of(Qualifier1.Literal.INSTANCE, NonQualifier.Literal.INSTANCE), MyEvent.class, Set.of()));

        assertThrows("An observedEventQualfiers annotation not a qualifier", IllegalArgumentException.class,
                () -> getCurrentBeanContainer().isMatchingEvent(MyEvent.class, Set.of(), MyEvent.class,
                        Set.of(Qualifier1.Literal.INSTANCE, NonQualifier.Literal.INSTANCE)));
    }

}
