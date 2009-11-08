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
package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * Tests for the InvocationContext implementation
 * @author Jozef Hartinger
 *
 */

@Artifact
@SpecVersion(spec = "int", version = "3.1.PFD")
public class InvocationContextTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "6", id = "d")
   public void testGetTargetMethod()
   {
      SimpleBean instance = getInstanceByType(SimpleBean.class);
      instance.setId(10);
      assert instance.getId() == 10;
      assert Interceptor1.isGetTargetOK();
   }

   @Test
   @SpecAssertion(section = "6", id = "f")
   public void testGetTimerMethod()
   {
      assert getInstanceByType(SimpleBean.class).testGetTimer();
   }

   @Test
   @SpecAssertion(section = "6", id = "g")
   public void testGetMethodMethod()
   {
      assert getInstanceByType(SimpleBean.class).testGetMethod();
   }

   @Test(groups = "ri-broken")
   @SpecAssertions( { 
      @SpecAssertion(section = "6", id = "i"), 
      @SpecAssertion(section = "6", id = "j"), 
      @SpecAssertion(section = "6", id = "k")
   })
   // WELD-275
   public void testMethodParameters()
   {
      assert getInstanceByType(SimpleBean.class).add(1, 2) == 5;
   }

   @Test(expectedExceptions = IllegalArgumentException.class, groups = "ri-broken")
   @SpecAssertion(section = "6", id = "la")
   // WELD-275
   public void testIllegalNumberOfParameters()
   {
      getInstanceByType(SimpleBean.class).add2(1, 1);
   }

   @Test(expectedExceptions = IllegalArgumentException.class, groups = "ri-broken")
   @SpecAssertion(section = "6", id = "lb")
   // WELD-275
   public void testIllegalTypeOfParameters()
   {
      assert getInstanceByType(SimpleBean.class).add3(1, 1) == 2;
   }

   @Test
   @SpecAssertion(section = "6", id = "o")
   public void testProceedReturnsNullForVoidMethod()
   {
      getInstanceByType(SimpleBean.class).voidMethod();
      assert Interceptor7.isProceedReturnsNull();
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "1", id = "d"),
      @SpecAssertion(section = "6", id = "a")
   })
   public void testContextData()
   {
      getInstanceByType(SimpleBean.class).foo();
      assert Interceptor8.isContextDataOK();
      assert Interceptor9.isContextDataOK();
   }
}
