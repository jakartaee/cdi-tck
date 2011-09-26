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
package org.jboss.jsr299.tck.tests.event.observer.resolve;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ResolveEventObserversTest extends AbstractJSR299Test {
    private static final String BEAN_MANAGER_RESOLVE_OBSERVERS_METHOD_NAME = "resolveObserverMethods";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ResolveEventObserversTest.class).build();
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "10.4", id = "e")
    public void testMultipleObserverMethodsForSameEventPermissible() {
        assert getCurrentManager().resolveObserverMethods(new DiskSpaceEvent()).size() == 2;
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "10.4", id = "f")
    public void testMultipleObserverMethodsOnBeanPermissible() {
        assert getCurrentManager().resolveObserverMethods(new BatteryEvent()).size() == 1;
        assert getCurrentManager().resolveObserverMethods(new DiskSpaceEvent()).size() == 2;
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "10.4.2", id = "a")
    public void testMethodWithParameterAnnotatedWithObservesRegistersObserverMethod() throws SecurityException,
            NoSuchMethodException {
        Set<ObserverMethod<? super Temperature>> temperatureObservers = getCurrentManager().resolveObserverMethods(
                new Temperature(0d));
        assert temperatureObservers.size() == 1;
        ObserverMethod<? super Temperature> temperatureObserver = temperatureObservers.iterator().next();
        assert temperatureObserver.getBeanClass().equals(AirConditioner.class);
        assert temperatureObserver.getObservedType().equals(Temperature.class);

        Method method = AirConditioner.class.getMethod("temperatureChanged", Temperature.class);
        assert method != null;
        assert method.getParameterTypes().length == 1;
        assert method.getParameterTypes()[0].equals(Temperature.class);
        assert method.getParameterAnnotations()[0][0].annotationType().equals(Observes.class);
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "10.4.1", id = "b")
    public void testObserverMethodWithoutBindingTypesObservesEventsWithoutBindingTypes() {
        // Resolve registered observers with an event containing no binding types
        assert getCurrentManager().resolveObserverMethods(new SimpleEventType()).size() == 2;
    }

    @Test(groups = { "events" })
    @SpecAssertions({ @SpecAssertion(section = "10.4.2", id = "c"), @SpecAssertion(section = "10.2.2", id = "a"),
            @SpecAssertion(section = "10.2.3", id = "a") })
    public void testObserverMethodMayHaveMultipleBindingTypes() {
        // If we can resolve the observer with the two binding types, then it worked
        assert getCurrentManager().resolveObserverMethods(new MultiBindingEvent(), new RoleBinding("Admin"),
                new TameAnnotationLiteral()).size() == 2;
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "10.5", id = "aa")
    public void testObserverMethodRegistration() {
        // Resolve registered observers with an event containing no binding types
        assert getCurrentManager().resolveObserverMethods(new SimpleEventType()).size() == 2;
    }

    @Test(groups = { "events" })
    @SpecAssertions({
            // these two assertions combine to create a logical, testable assertion
            @SpecAssertion(section = "11.3.10", id = "a"), @SpecAssertion(section = "11.3.10", id = "b") })
    public void testBeanManagerResolveObserversSignature() throws Exception {
        assert getCurrentManager().getClass().getDeclaredMethod(BEAN_MANAGER_RESOLVE_OBSERVERS_METHOD_NAME, Object.class,
                Annotation[].class) != null;
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "12.3", id = "o")
    public void testObserverMethodAutomaticallyRegistered() {
        assert !getCurrentManager().resolveObserverMethods(new String(), new AnnotationLiteral<Secret>() {
        }).isEmpty();
    }

    @Test(groups = { "events" })
    @SpecAssertion(section = "12.3", id = "o")
    public void testObserverMethodNotAutomaticallyRegisteredForDisabledBeans() {
        Set<ObserverMethod<? super Ghost>> ghostObservers = getCurrentManager().resolveObserverMethods(new Ghost());
        assert ghostObservers.size() == 0;

        Set<ObserverMethod<? super String>> stringObservers = getCurrentManager().resolveObserverMethods(new String(),
                new AnnotationLiteral<Secret>() {
                });
        assert stringObservers.size() == 1;
        for (ObserverMethod<? super String> observer : stringObservers) {
            // an assertion error will be raised if an inappropriate observer is called
            observer.notify("fail if disabled observer invoked");
        }
    }

}
