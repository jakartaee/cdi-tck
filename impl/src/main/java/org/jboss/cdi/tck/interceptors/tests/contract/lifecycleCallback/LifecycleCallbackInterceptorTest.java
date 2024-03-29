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
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.CONSTRUCTOR_AND_METHOD_LEVEL_INT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class LifecycleCallbackInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(LifecycleCallbackInterceptorTest.class).build();
    }

    @Test
    @SpecAssertion(section = DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS, id = "ab")
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "b")
    public void testPostConstructInterceptor() {
        getContextualReference(Goat.class);
        assertTrue(Goat.isPostConstructInterceptorCalled());
        assertTrue(AnimalInterceptor.isPostConstructInterceptorCalled(Goat.GOAT));
        getContextualReference(Hen.class).toString();
        assertTrue(Hen.isPostConstructInterceptorCalled());
        assertTrue(AnimalInterceptor.isPostConstructInterceptorCalled(Hen.HEN));
        getContextualReference(Cow.class).toString();
        assertTrue(Cow.isPostConstructInterceptorCalled());
        assertTrue(AnimalInterceptor.isPostConstructInterceptorCalled(Cow.COW));
    }

    @Test
    @SpecAssertion(section = DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS, id = "ab")
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "c")
    public void testPreDestroyInterceptor() {
        createAndDestroyInstance(Goat.class);
        assertTrue(Goat.isPreDestroyInterceptorCalled());
        assertTrue(AnimalInterceptor.isPreDestroyInterceptorCalled(Goat.GOAT));
        createAndDestroyInstance(Hen.class);
        assertTrue(Hen.isPreDestroyInterceptorCalled());
        assertTrue(AnimalInterceptor.isPreDestroyInterceptorCalled(Hen.HEN));
        createAndDestroyInstance(Cow.class);
        assertTrue(Cow.isPreDestroyInterceptorCalled());
        assertTrue(AnimalInterceptor.isPreDestroyInterceptorCalled(Cow.COW));
    }

    @Test
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "f")
    public void testSingleMethodInterposingMultipleLifecycleCallbackEvents() {
        AlmightyLifecycleInterceptor.reset();
        Dog.reset();
        createAndDestroyInstance(Dog.class);
        assertEquals(AlmightyLifecycleInterceptor.getNumberOfInterceptions(), 3);
        assertEquals(Dog.getNumberOfInterceptions(), 2);
    }

    @SuppressWarnings("unchecked")
    private <T extends Animal> void createAndDestroyInstance(Class<T> clazz) {
        Bean<T> bean = getUniqueBean(clazz);
        CreationalContext<T> ctx = getCurrentManager().createCreationalContext(bean);
        T instance = (T) getCurrentManager().getReference(bean, clazz, ctx);
        instance.foo(); // invoke method so that the instance is actually created
        // destroy the instance
        bean.destroy(instance, ctx);
    }

    @Test
    @SpecAssertion(section = DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS, id = "ab")
    @SpecAssertion(section = DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS, id = "d")
    public void testAroundInvokeAndLifeCycleCallbackInterceptorsCanBeDefinedOnTheSameClass() {
        assertEquals(getContextualReference(Goat.class).echo("foo"), "foofoo");
    }

    @Test
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "ja")
    public void testPublicLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assertTrue(PublicLifecycleInterceptor.isIntercepted());
    }

    @Test
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "jc")
    public void testProtectedLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assertTrue(ProtectedLifecycleInterceptor.isIntercepted());
    }

    @Test
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "jb")
    public void testPrivateLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assertTrue(PrivateLifecycleInterceptor.isIntercepted());
    }

    @Test
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "jd")
    public void testPackagePrivateLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assertTrue(PackagePrivateLifecycleInterceptor.isIntercepted());
    }

    @Test
    @SpecAssertion(section = CONSTRUCTOR_AND_METHOD_LEVEL_INT, id = "b")
    public void testLifeCycleCallbackInterceptorNotInvokedForMethodLevelInterceptor() {
        assertEquals(getContextualReference(Sheep.class).foo(), "bar");
        assertTrue(SheepInterceptor.isAroundInvokeCalled());
        assertFalse(SheepInterceptor.isPostConstructCalled());
    }

}
