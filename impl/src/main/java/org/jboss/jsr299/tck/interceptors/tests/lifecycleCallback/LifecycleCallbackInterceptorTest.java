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
package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec = "int", version = "3.1.PFD")
public class LifecycleCallbackInterceptorTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "5", id = "a")
   // WELD-436
   public void testPostConstructInterceptor()
   {
      getInstanceByType(Goat.class);
      assert Goat.isPostConstructInterceptorCalled();
      assert AnimalInterceptor.isPostConstructInterceptorCalled(Goat.class);
      getInstanceByType(Hen.class);
      assert Hen.isPostConstructInterceptorCalled();
      assert AnimalInterceptor.isPostConstructInterceptorCalled(Hen.class);
      getInstanceByType(Cow.class);
      assert Cow.isPostConstructInterceptorCalled();
      assert AnimalInterceptor.isPostConstructInterceptorCalled(Cow.class);
   }

   @Test
   @SpecAssertion(section = "5", id = "a")
   // WELD-436
   public void testPreDestroyInterceptor()
   {
      createAndDestroyInstance(Goat.class);
      assert Goat.isPreDestroyInterceptorCalled();
      assert AnimalInterceptor.isPreDestroyInterceptorCalled(Goat.class);
      createAndDestroyInstance(Hen.class);
      assert Hen.isPreDestroyInterceptorCalled();
      assert AnimalInterceptor.isPreDestroyInterceptorCalled(Hen.class);
      createAndDestroyInstance(Hen.class);
      assert Hen.isPreDestroyInterceptorCalled();
      assert AnimalInterceptor.isPreDestroyInterceptorCalled(Hen.class);
   }

   @SuppressWarnings("unchecked")
   private <T> void createAndDestroyInstance(Class<T> clazz)
   {
      Bean<T> bean = getBeans(clazz).iterator().next();
      CreationalContext<T> ctx = getCurrentManager().createCreationalContext(bean);
      T instance = (T) getCurrentManager().getReference(bean, clazz, ctx);
      // destroy the instance
      bean.destroy(instance, ctx);
   }

   @Test
   @SpecAssertion(section = "5", id = "c")
   public void testAroundInvokeAndLifeCycleCallbackInterceptorsCanBeDefinedOnTheSameClass()
   {
      assert getInstanceByType(Goat.class).echo("foo").equals("foofoo");
   }

   @Test
   @SpecAssertion(section = "5", id = "j")
   public void testPublicLifecycleInterceptorMethod()
   {
      getInstanceByType(Chicken.class);
      assert PublicLifecycleInterceptor.isIntercepted();
   }

   @Test
   @SpecAssertion(section = "5", id = "k")
   public void testProtectedLifecycleInterceptorMethod()
   {
      getInstanceByType(Chicken.class);
      assert ProtectedLifecycleInterceptor.isIntercepted();
   }

   @Test
   @SpecAssertion(section = "5", id = "l")
   public void testPrivateLifecycleInterceptorMethod()
   {
      getInstanceByType(Chicken.class);
      assert PrivateLifecycleInterceptor.isIntercepted();
   }

   @Test
   @SpecAssertion(section = "5", id = "m")
   public void testPackagePrivateLifecycleInterceptorMethod()
   {
      getInstanceByType(Chicken.class);
      assert PackagePrivateLifecycleInterceptor.isIntercepted();
   }

   @Test
   @SpecAssertion(section = "8", id = "c")
   public void testLifeCycleCallbackInterceptorNotInvokedForMethodLevelInterceptor()
   {
      assert getInstanceByType(Sheep.class).foo().equals("bar");
      assert SheepInterceptor.isAroundInvokeCalled();
      assert !SheepInterceptor.isPostConstructCalled();
   }
}
