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
package org.jboss.cdi.tck.tests.event.fires;

import static org.jboss.cdi.tck.TestGroups.REWRITE;
import static org.jboss.cdi.tck.cdi.Sections.BM_FIRE_EVENT;
import static org.jboss.cdi.tck.cdi.Sections.EVENT;
import static org.jboss.cdi.tck.cdi.Sections.FIRING_EVENTS;
import static org.jboss.cdi.tck.cdi.Sections.FIRING_EVENTS_SYNCHRONOUSLY;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests that verify the event firing behavior of the Event interface.
 *
 * @author Dan Allen
 * @author Martin Kouba
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0")
public class FireEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(FireEventTest.class).build();
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_FIRE_EVENT, id = "a"), @SpecAssertion(section = BM_FIRE_EVENT, id = "b") })
    public void testBeanManagerFireEvent() {
        Billing billing = getContextualReference(Billing.class);
        billing.reset();
        MiniBar miniBar = new MiniBar();
        miniBar.stockNoNotify();
        getCurrentManager().getEvent().select(MiniBar.class).fire(miniBar);
        assertTrue(billing.isActive());
        Item chocolate = miniBar.getItemByName("Chocolate");
        getCurrentManager().getEvent().select(Item.class, new Lifted.LiftedLiteral() {
        }).fire(chocolate);
        assertEquals(billing.getCharge(), 5);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = BM_FIRE_EVENT, id = "c")
    public void testTypeVariableEventTypeFails() throws Exception {
        getContextualReference(Bar.class).<Integer> fireWithTypeVariable();
    }

    @SuppressWarnings("serial")
    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = BM_FIRE_EVENT, id = "d")
    public void testDuplicateBindingsToFireEventFails() throws Exception {
        getCurrentManager().getEvent().select(new Lifted.LiftedLiteral("a") {
        }, new Lifted.LiftedLiteral("b") {
        }).fire(new Object());
    }

    /**
     * This test verifies that the {@link Event} object capable of firing {@link Item} objects can be injected with the {@link
     * Any} binding type and that the injected object can be used to fire an event. The functionality is verified by checking
     * that the corresponding observer gets invoked.
     */
    // Simplify assertions
    @Test(groups = REWRITE)
    @SpecAssertion(section = FIRING_EVENTS, id = "a")
    public void testInjectedAnyEventCanFireEvent() {
        Billing billing = getContextualReference(Billing.class);
        billing.reset();
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);
        InjectionPoint eventInjection = null;
        for (InjectionPoint candidate : miniBarBean.getInjectionPoints()) {
            if (candidate.getMember().getName().equals("miniBarEvent")) {
                eventInjection = candidate;
                break;
            }
        }

        assertNotNull(eventInjection);
        assertEquals(eventInjection.getQualifiers().size(), 1);
        assertTrue(eventInjection.getQualifiers().contains(Any.Literal.INSTANCE));

        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);
        miniBar.stock();
        assertTrue(billing.isActive());
        assertEquals(billing.getMiniBarValue(), 16);
    }

    /**
     * This test verifies that the fire() method of the injected {@link Event} object accepts an event object and that the event
     * object's type is the same as the the parameterized type on the event field.
     **/
    // Simplify assertions
    @Test(groups = REWRITE)
    @SpecAssertions({ @SpecAssertion(section = FIRING_EVENTS_SYNCHRONOUSLY, id = "b"),
            @SpecAssertion(section = EVENT, id = "cb") })
    public void testInjectedEventAcceptsEventObject() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
        Billing billing = getContextualReference(Billing.class);
        billing.reset();
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);
        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);

        Field eventField = miniBar.getClass().getDeclaredField("miniBarEvent");
        ParameterizedType eventFieldType = (ParameterizedType) eventField.getGenericType();
        assertEquals(eventFieldType.getActualTypeArguments().length, 1);
        assertEquals(MiniBar.class, eventFieldType.getActualTypeArguments()[0]);
        assertEquals(Event.class, eventFieldType.getRawType());
        Method fireMethod = null;
        @SuppressWarnings("unchecked")
        Class<Event<Item>> eventFieldClass = (Class<Event<Item>>) eventFieldType.getRawType();
        for (Method method : eventFieldClass.getMethods()) {
            if (method.getName().equals("fire") && !method.isSynthetic()) {
                if (fireMethod != null) {
                    fail("Expecting exactly one method on Event named 'fire'");
                }
                fireMethod = method;
            }
        }

        if (fireMethod == null) {
            fail("Expecting exactly one method on Event named 'fire'");
        }

        assertEquals(fireMethod.getParameterTypes().length, 1);
        assertEquals(fireMethod.getGenericParameterTypes().length, 1);
        // make sure the same type used to parameterize the Event class is referenced in the fire() method
        Type fireMethodArgumentType = fireMethod.getGenericParameterTypes()[0];
        @SuppressWarnings("rawtypes")
        Type eventClassParameterizedType = ((TypeVariable) fireMethod.getGenericParameterTypes()[0]).getGenericDeclaration()
                .getTypeParameters()[0];
        assertEquals(fireMethodArgumentType, eventClassParameterizedType);

        miniBar.stock();
        assertTrue(billing.isActive());
        assertEquals(billing.getMiniBarValue(), 16);
    }

    /**
     * This test verifies that the {@link Event} object representing an {@link Item} with the {@link Lifted} binding type is
     * properly injected and that this object can be used to fire an event. The functionality is verified by checking that the
     * cooresponding observer gets invoked.
     */
    @SuppressWarnings("serial")
    // Simplify assertions
    @Test(groups = REWRITE)
    @SpecAssertions({ @SpecAssertion(section = FIRING_EVENTS, id = "c"), @SpecAssertion(section = EVENT, id = "cb") })
    public void testInjectedEventCanHaveBindings() {
        Billing billing = getContextualReference(Billing.class);
        billing.reset();
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);

        InjectionPoint eventInjection = null;
        for (InjectionPoint candidate : miniBarBean.getInjectionPoints()) {
            if (candidate.getMember().getName().equals("itemLiftedEvent")) {
                eventInjection = candidate;
                break;
            }
        }

        assertNotNull(eventInjection);
        assertEquals(eventInjection.getQualifiers().size(), 1);
        assertTrue(eventInjection.getQualifiers().contains(new Lifted.LiftedLiteral() {
        }));

        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);
        miniBar.stock();
        Item chocolate = miniBar.getItemByName("Chocolate");
        assertNotNull(chocolate);
        miniBar.liftItem(chocolate);
        assertEquals(billing.getCharge(), chocolate.getPrice());
    }

    /**
     * This test verifies that binding types can be specified dynamically when firing an event using {@code Event#fire()} by
     * first using the {@code Event#select()} method to retrieve an Event object with associated binding types.
     */
    // Simplify assertions
    @Test(groups = REWRITE)
    @SpecAssertion(section = FIRING_EVENTS, id = "d")
    public void testInjectedEventCanSpecifyBindingsDynamically() {
        Billing billing = getContextualReference(Billing.class);
        billing.reset();
        Housekeeping housekeeping = getContextualReference(Housekeeping.class);
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);

        InjectionPoint eventInjection = null;
        for (InjectionPoint candidate : miniBarBean.getInjectionPoints()) {
            if (candidate.getMember().getName().equals("itemEvent")) {
                eventInjection = candidate;
                break;
            }
        }

        assertNotNull(eventInjection);
        assertEquals(eventInjection.getQualifiers().size(), 1);
        assertTrue(eventInjection.getQualifiers().contains(Any.Literal.INSTANCE));
        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);
        miniBar.stock();
        Item water = miniBar.liftItemByName("16 oz Water");
        miniBar.restoreItem(water);
        assertEquals(billing.getCharge(), 1);
        assertEquals(housekeeping.getItemsTainted().size(), 1);
        assertTrue(housekeeping.getItemsTainted().contains(water));
    }

    @Test
    @SpecAssertion(section = EVENT, id = "ca")
    public void testEventProvidesMethodForFiringEventsWithCombinationOfTypeAndBindings() {
        DoggiePoints points = getContextualReference(DoggiePoints.class);
        points.reset();
        DogWhisperer master = getContextualReference(DogWhisperer.class);
        master.issueTamingCommand();
        assertEquals(points.getNumTamed(), 1);
        assertEquals(points.getNumPraiseReceived(), 0);
        master.givePraise();
        assertEquals(points.getNumTamed(), 1);
        assertEquals(points.getNumPraiseReceived(), 1);
    }

    // Simplify assertions
    @SuppressWarnings("serial")
    @Test(groups = REWRITE)
    @SpecAssertion(section = EVENT, id = "eda")
    public void testEventSelectedFiresAndObserversNotified() {
        Housekeeping houseKeeping = getContextualReference(Housekeeping.class);
        houseKeeping.reset();
        MiniBar miniBar = getContextualReference(MiniBar.class);
        Item chocolate = new Item("Chocolate", 5);
        Item crackers = new Item("Crackers", 2);

        miniBar.getItemEvent().fire(chocolate);
        assertEquals(houseKeeping.getItemActivity().size(), 1);
        assertEquals(houseKeeping.getItemActivity().get(0), chocolate);

        miniBar.getItemEvent().select(new Lifted.LiftedLiteral() {
        }).fire(crackers);
        assertEquals(houseKeeping.getItemActivity().size(), 2);
        assertEquals(houseKeeping.getItemActivity().get(1), crackers);
        assertEquals(houseKeeping.getItemsMissing().size(), 1);
        assertEquals(houseKeeping.getItemsMissing().iterator().next(), crackers);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = EVENT, id = "f"), @SpecAssertion(section = OBSERVER_RESOLUTION, id = "j") })
    public <T> void testEventFireThrowsExceptionIfEventObjectTypeContainsUnresovableTypeVariable() {
        MiniBar miniBar = getContextualReference(MiniBar.class);
        miniBar.itemEvent.fire(new Item_Illegal<T>("12 oz Beer", 6));
    }

}
