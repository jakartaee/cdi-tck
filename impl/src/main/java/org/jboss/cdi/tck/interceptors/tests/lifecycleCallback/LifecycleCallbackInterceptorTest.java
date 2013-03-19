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
package org.jboss.cdi.tck.interceptors.tests.lifecycleCallback;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "int", version = "3.1.PFD")
public class LifecycleCallbackInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(LifecycleCallbackInterceptorTest.class).build();
    }

    @Test
    @SpecAssertion(section = "5", id = "a")
    public void testPostConstructInterceptor() {
        getContextualReference(Goat.class);
        assert Goat.isPostConstructInterceptorCalled();
        assert AnimalInterceptor.isPostConstructInterceptorCalled(Goat.GOAT);
        getContextualReference(Hen.class).toString();
        assert Hen.isPostConstructInterceptorCalled();
        assert AnimalInterceptor.isPostConstructInterceptorCalled(Hen.HEN);
        getContextualReference(Cow.class).toString();
        assert Cow.isPostConstructInterceptorCalled();
        assert AnimalInterceptor.isPostConstructInterceptorCalled(Cow.COW);
    }

    @Test
    @SpecAssertion(section = "5", id = "a")
    // WELD-436
    public void testPreDestroyInterceptor() {
        createAndDestroyInstance(Goat.class);
        assert Goat.isPreDestroyInterceptorCalled();
        assert AnimalInterceptor.isPreDestroyInterceptorCalled(Goat.GOAT);
        createAndDestroyInstance(Hen.class);
        assert Hen.isPreDestroyInterceptorCalled();
        assert AnimalInterceptor.isPreDestroyInterceptorCalled(Hen.HEN);
        createAndDestroyInstance(Cow.class);
        assert Cow.isPreDestroyInterceptorCalled();
        assert AnimalInterceptor.isPreDestroyInterceptorCalled(Cow.COW);
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
    @SpecAssertion(section = "5", id = "c")
    public void testAroundInvokeAndLifeCycleCallbackInterceptorsCanBeDefinedOnTheSameClass() {
        assert getContextualReference(Goat.class).echo("foo").equals("foofoo");
    }

    @Test
    @SpecAssertion(section = "5", id = "j")
    public void testPublicLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assert PublicLifecycleInterceptor.isIntercepted();
    }

    @Test
    @SpecAssertion(section = "5", id = "k")
    public void testProtectedLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assert ProtectedLifecycleInterceptor.isIntercepted();
    }

    @Test
    @SpecAssertion(section = "5", id = "l")
    public void testPrivateLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assert PrivateLifecycleInterceptor.isIntercepted();
    }

    @Test
    @SpecAssertion(section = "5", id = "m")
    public void testPackagePrivateLifecycleInterceptorMethod() {
        getContextualReference(Chicken.class);
        assert PackagePrivateLifecycleInterceptor.isIntercepted();
    }

    @Test
    @SpecAssertion(section = "8", id = "c")
    public void testLifeCycleCallbackInterceptorNotInvokedForMethodLevelInterceptor() {
        assert getContextualReference(Sheep.class).foo().equals("bar");
        assert SheepInterceptor.isAroundInvokeCalled();
        assert !SheepInterceptor.isPostConstructCalled();
    }
}
