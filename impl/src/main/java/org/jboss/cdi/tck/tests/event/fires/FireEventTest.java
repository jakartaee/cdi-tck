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
package org.jboss.cdi.tck.tests.event.fires;

import static org.jboss.cdi.tck.TestGroups.EVENTS;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
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
@SpecVersion(spec = "cdi", version = "20091101")
public class FireEventTest extends AbstractTest {
    private static final String BEAN_MANAGER_FIRE_EVENT_METHOD_NAME = "fireEvent";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(FireEventTest.class).build();
    }

    @Test(groups = { EVENTS })
    @SpecAssertions({
            // these two assertions combine to create a logical, testable assertion
            @SpecAssertion(section = "11.3.10", id = "a"), @SpecAssertion(section = "11.3.10", id = "b") })
    public void testBeanManagerInterfaceForFireEventMethod() throws Exception {
        assert BeanManager.class.getDeclaredMethod(BEAN_MANAGER_FIRE_EVENT_METHOD_NAME, Object.class, Annotation[].class) != null;
    }

    @Test(groups = { EVENTS })
    @SpecAssertion(section = "11.3.10", id = "a")
    public void testBeanManagerFireEvent() {
        Billing billing = getInstanceByType(Billing.class);
        billing.reset();
        MiniBar miniBar = new MiniBar();
        miniBar.stockNoNotify();
        getCurrentManager().fireEvent(miniBar);
        assert billing.isActive();
        Item chocolate = miniBar.getItemByName("Chocolate");
        getCurrentManager().fireEvent(chocolate, new AnnotationLiteral<Lifted>() {
        });
        assert billing.getCharge() == 5.00d;
    }

    @Test(groups = { EVENTS }, expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = "11.3.10", id = "c")
    public void testTypeVariableEventTypeFails() throws Exception {
        getInstanceByType(Bar.class).<Integer> fireWithTypeVariable();
    }

    @Test(groups = { EVENTS }, expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = "11.3.10", id = "d")
    public void testDuplicateBindingsToFireEventFails() throws Exception {
        getCurrentManager().fireEvent(new Object(), new AnnotationLiteral<Lifted>() {
        }, new AnnotationLiteral<Lifted>() {
        });
    }

/**
     * This test verifies that the {@link Event} object capable of firing {@link Item} objects can be injected with the {@link
     * @Any} binding type and that the injected object can be used to fire an event. The functionality is verified by checking
     * that the corresponding observer gets invoked.
     */
    @Test(groups = EVENTS)
    @SpecAssertion(section = "10.3", id = "a")
    public void testInjectedAnyEventCanFireEvent() {
        Billing billing = getInstanceByType(Billing.class);
        billing.reset();
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);
        InjectionPoint eventInjection = null;
        for (InjectionPoint candidate : miniBarBean.getInjectionPoints()) {
            if (candidate.getMember().getName().equals("miniBarEvent")) {
                eventInjection = candidate;
                break;
            }
        }

        assert eventInjection != null;
        assert eventInjection.getQualifiers().size() == 1;
        assert eventInjection.getQualifiers().contains(AnyLiteral.INSTANCE);

        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);
        miniBar.stock();
        assert billing.isActive();
        assert billing.getMiniBarValue() == 16.00d;
    }

    /**
     * This test verifies that the fire() method of the injected {@link Event} object accepts an event object and that the event
     * object's type is the same as the the parameterized type on the event field.
     **/
    @SpecAssertions({ @SpecAssertion(section = "10.3", id = "b"), @SpecAssertion(section = "10.3.1", id = "cb") })
    @Test(groups = EVENTS)
    public void testInjectedEventAcceptsEventObject() throws SecurityException, NoSuchFieldException, NoSuchMethodException {
        Billing billing = getInstanceByType(Billing.class);
        billing.reset();
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);
        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);

        Field eventField = miniBar.getClass().getDeclaredField("miniBarEvent");
        ParameterizedType eventFieldType = (ParameterizedType) eventField.getGenericType();
        assert eventFieldType.getActualTypeArguments().length == 1;
        assert MiniBar.class.equals(eventFieldType.getActualTypeArguments()[0]);
        assert Event.class.equals(eventFieldType.getRawType());
        Method fireMethod = null;
        @SuppressWarnings("unchecked")
        Class<Event<Item>> eventFieldClass = (Class<Event<Item>>) eventFieldType.getRawType();
        for (Method method : eventFieldClass.getMethods()) {
            if (method.getName().equals("fire") && !method.isSynthetic()) {
                if (fireMethod != null) {
                    assert false : "Expecting exactly one method on Event named 'fire'";
                }
                fireMethod = method;
            }
        }

        if (fireMethod == null) {
            assert false : "Expecting exactly one method on Event named 'fire'";
        }

        assert fireMethod.getParameterTypes().length == 1;
        assert fireMethod.getGenericParameterTypes().length == 1;
        // make sure the same type used to parameterize the Event class is referenced in the fire() method
        Type fireMethodArgumentType = fireMethod.getGenericParameterTypes()[0];
        @SuppressWarnings("unchecked")
        Type eventClassParameterizedType = ((TypeVariable) fireMethod.getGenericParameterTypes()[0]).getGenericDeclaration()
                .getTypeParameters()[0];
        assert fireMethodArgumentType.equals(eventClassParameterizedType);

        miniBar.stock();
        assert billing.isActive();
        assert billing.getMiniBarValue() == 16.00d;
    }

    /**
     * This test verifies that the {@link Event} object representing an {@link Item} with the {@link @Lifted} binding type is
     * properly injected and that this object can be used to fire an event. The functionality is verified by checking that the
     * cooresponding observer gets invoked.
     */
    @Test(groups = EVENTS)
    @SpecAssertions({ @SpecAssertion(section = "10.3", id = "c"), @SpecAssertion(section = "10.3.1", id = "cb") })
    public void testInjectedEventCanHaveBindings() {
        Billing billing = getInstanceByType(Billing.class);
        billing.reset();
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);

        InjectionPoint eventInjection = null;
        for (InjectionPoint candidate : miniBarBean.getInjectionPoints()) {
            if (candidate.getMember().getName().equals("itemLiftedEvent")) {
                eventInjection = candidate;
                break;
            }
        }

        assert eventInjection != null;
        assert eventInjection.getQualifiers().size() == 1;
        assert eventInjection.getQualifiers().contains(new AnnotationLiteral<Lifted>() {
        });

        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);
        miniBar.stock();
        Item chocolate = miniBar.getItemByName("Chocolate");
        assert chocolate != null;
        miniBar.liftItem(chocolate);
        assert billing.getCharge() == chocolate.getPrice();
    }

    /**
     * This test verifies that binding types can be specified dynamically when firing an event using {@link Event#fire()} by
     * first using the {@link Event#select()} method to retrieve an Event object with associated binding types.
     */
    @Test(groups = EVENTS)
    @SpecAssertion(section = "10.3", id = "d")
    public void testInjectedEventCanSpecifyBindingsDynamically() {
        Billing billing = getInstanceByType(Billing.class);
        billing.reset();
        Housekeeping housekeeping = getInstanceByType(Housekeeping.class);
        Bean<MiniBar> miniBarBean = getUniqueBean(MiniBar.class);

        InjectionPoint eventInjection = null;
        for (InjectionPoint candidate : miniBarBean.getInjectionPoints()) {
            if (candidate.getMember().getName().equals("itemEvent")) {
                eventInjection = candidate;
                break;
            }
        }

        assert eventInjection != null;
        assert eventInjection.getQualifiers().size() == 1;
        assert eventInjection.getQualifiers().contains(AnyLiteral.INSTANCE);
        CreationalContext<MiniBar> miniBarCc = getCurrentManager().createCreationalContext(miniBarBean);
        MiniBar miniBar = miniBarBean.create(miniBarCc);
        miniBar.stock();
        Item water = miniBar.liftItemByName("16 oz Water");
        miniBar.restoreItem(water);
        assert billing.getCharge() == 1.00d;
        assert housekeeping.getItemsTainted().size() == 1;
        assert housekeeping.getItemsTainted().contains(water);
    }

    @Test(groups = EVENTS)
    @SpecAssertion(section = "10.3.1", id = "ca")
    public void testEventProvidesMethodForFiringEventsWithCombinationOfTypeAndBindings() {
        DoggiePoints points = getInstanceByType(DoggiePoints.class);
        points.reset();
        DogWhisperer master = getInstanceByType(DogWhisperer.class);
        master.issueTamingCommand();
        assert points.getNumTamed() == 1;
        assert points.getNumPraiseReceived() == 0;
        master.givePraise();
        assert points.getNumTamed() == 1;
        assert points.getNumPraiseReceived() == 1;
    }

    @Test(groups = EVENTS)
    @SpecAssertion(section = "10.3.1", id = "eda")
    public void testEventSelectedFiresAndObserversNotified() {
        Housekeeping houseKeeping = getInstanceByType(Housekeeping.class);
        houseKeeping.reset();
        MiniBar miniBar = getInstanceByType(MiniBar.class);
        Item chocolate = new Item("Chocolate", 5.00d);
        Item crackers = new Item("Crackers", 2.50d);

        miniBar.getItemEvent().fire(chocolate);
        assert houseKeeping.getItemActivity().size() == 1;
        assert houseKeeping.getItemActivity().get(0) == chocolate;

        miniBar.getItemEvent().select(new AnnotationLiteral<Lifted>() {
        }).fire(crackers);
        assert houseKeeping.getItemActivity().size() == 2;
        assert houseKeeping.getItemActivity().get(1) == crackers;
        assert houseKeeping.getItemsMissing().size() == 1;
        assert houseKeeping.getItemsMissing().iterator().next() == crackers;
    }

    @Test(groups = { EVENTS }, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = "10.3.1", id = "f"), @SpecAssertion(section = "10.2", id = "j") })
    public <T> void testEventFireThrowsExceptionIfEventObjectContainsTypeVariable() {
        MiniBar miniBar = getInstanceByType(MiniBar.class);
        miniBar.itemEvent.fire(new Item_Illegal<T>("12 oz Beer", 5.50));
    }

}
