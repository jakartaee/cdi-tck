/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
   public void testPostConstructInterceptor()
   {
      getInstanceByType(Goat.class);
      assert Goat.isPostConstructInterceptorCalled();
      assert GoatInterceptor.isPostConstructInterceptorCalled();
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "5", id = "a")
   // WELD-279
   public void testPreDestroyInterceptor()
   {
      // create the instance
      Bean<Goat> bean = getBeans(Goat.class).iterator().next();
      CreationalContext<Goat> ctx = getCurrentManager().createCreationalContext(bean);
      Goat instance = (Goat) getCurrentManager().getReference(bean, Goat.class, ctx);
      // destroy the instance
      bean.destroy(instance, ctx);
      assert Goat.isPreDestroyInterceptorCalled();
      assert GoatInterceptor.isPreDestroyInterceptorCalled();
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
