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
package org.jboss.cdi.tck.tests.event.observer.resolve;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBSERVER_METHOD_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.EVENT_QUALIFIER_TYPES_WITH_MEMBERS;
import static org.jboss.cdi.tck.cdi.Sections.MULTIPLE_EVENT_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHODS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD_EVENT_PARAMETER;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ResolveEventObserversTest extends AbstractTest {
    private static final String BEAN_MANAGER_RESOLVE_OBSERVERS_METHOD_NAME = "resolveObserverMethods";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ResolveEventObserversTest.class).build();
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHODS, id = "e")
    public void testMultipleObserverMethodsForSameEventPermissible() {
        assertEquals(getCurrentManager().resolveObserverMethods(new DiskSpaceEvent()).size(), 2);
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHODS, id = "f")
    public void testMultipleObserverMethodsOnBeanPermissible() {
        assertEquals(getCurrentManager().resolveObserverMethods(new BatteryEvent()).size(), 1);
        assertEquals(getCurrentManager().resolveObserverMethods(new DiskSpaceEvent()).size(), 2);
    }

    @Test
    @SpecAssertion(section = OBSERVES, id = "a")
    public void testMethodWithParameterAnnotatedWithObservesRegistersObserverMethod() throws SecurityException,
            NoSuchMethodException {
        Set<ObserverMethod<? super Temperature>> temperatureObservers = getCurrentManager().resolveObserverMethods(
                new Temperature(0d));
        assertEquals(temperatureObservers.size(), 1);
        ObserverMethod<? super Temperature> temperatureObserver = temperatureObservers.iterator().next();
        assertEquals(temperatureObserver.getBeanClass(), AirConditioner.class);
        assertEquals(temperatureObserver.getObservedType(), Temperature.class);

        Method method = AirConditioner.class.getMethod("temperatureChanged", Temperature.class);
        assertNotNull(method);
        assertEquals(method.getParameterTypes().length, 1);
        assertEquals(method.getParameterTypes()[0], Temperature.class);
        assertEquals(method.getParameterAnnotations()[0][0].annotationType(), Observes.class);
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD_EVENT_PARAMETER, id = "b")
    public void testObserverMethodWithoutBindingTypesObservesEventsWithoutBindingTypes() {
        // Resolve registered observers with an event containing no binding types
        assertEquals(getCurrentManager().resolveObserverMethods(new SimpleEventType()).size(), 2);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVES, id = "c"), @SpecAssertion(section = EVENT_QUALIFIER_TYPES_WITH_MEMBERS, id = "a"),
            @SpecAssertion(section = MULTIPLE_EVENT_QUALIFIERS, id = "a") })
    public void testObserverMethodMayHaveMultipleBindingTypes() {
        // If we can resolve the observer with the two binding types, then it worked
        assertEquals(getCurrentManager().resolveObserverMethods(new MultiBindingEvent(), new RoleBinding("Admin"), new TameAnnotationLiteral()).size(), 2);
    }

    @Test
    @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "aa")
    public void testObserverMethodRegistration() {
        // Resolve registered observers with an event containing no binding types
        assertEquals(getCurrentManager().resolveObserverMethods(new SimpleEventType()).size(), 2);
    }

    @Test
    @SpecAssertions({
            // these two assertions combine to create a logical, testable assertion
            @SpecAssertion(section = BM_OBSERVER_METHOD_RESOLUTION, id = "a"), @SpecAssertion(section = BM_OBSERVER_METHOD_RESOLUTION, id = "b") })
    public void testBeanManagerResolveObserversSignature() throws Exception {
        assertNotNull(getCurrentManager().getClass().getDeclaredMethod(BEAN_MANAGER_RESOLVE_OBSERVERS_METHOD_NAME, Object.class, Annotation[].class));
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = BM_OBSERVER_METHOD_RESOLUTION, id = "e")
    public void testBeanManagerResolveObserversWithIllegalQualifier() {
        getCurrentManager().resolveObserverMethods(new SimpleEventType(), new AnnotationLiteral<Override>() {
        });
    }

    @Test
    @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "o")
    public void testObserverMethodAutomaticallyRegistered() {
        assertFalse(getCurrentManager().resolveObserverMethods(new String(), new AnnotationLiteral<Secret>() {
        }).isEmpty());
    }

    @Test
    @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "o")
    public void testObserverMethodNotAutomaticallyRegisteredForDisabledBeans() {
        Set<ObserverMethod<? super Ghost>> ghostObservers = getCurrentManager().resolveObserverMethods(new Ghost());
        assertEquals(ghostObservers.size(), 0);

        Set<ObserverMethod<? super String>> stringObservers = getCurrentManager().resolveObserverMethods(new String(),
                new AnnotationLiteral<Secret>() {
                });
        assertEquals(stringObservers.size(), 1);
        for (ObserverMethod<? super String> observer : stringObservers) {
            // an assertion error will be raised if an inappropriate observer is called
            observer.notify("fail if disabled observer invoked");
        }
    }

    @Test
    @SpecAssertion(section = OBSERVES, id = "ab")
    public void testSyncObserver() {
        Set<ObserverMethod<? super DiskSpaceEvent>> diskSpaceObservers = getCurrentManager().resolveObserverMethods(new DiskSpaceEvent());
        assertTrue(diskSpaceObservers.stream().allMatch(method -> !method.isAsync()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVES, id = "ac"), @SpecAssertion(section = OBSERVER_METHOD, id = "g") })
    public void testAsyncObserver() {
        Set<ObserverMethod<? super User>> userObservers = getCurrentManager().resolveObserverMethods(new User());
        assertEquals(userObservers.size(), 1);
        assertTrue(userObservers.iterator().next().isAsync());
    }

}
