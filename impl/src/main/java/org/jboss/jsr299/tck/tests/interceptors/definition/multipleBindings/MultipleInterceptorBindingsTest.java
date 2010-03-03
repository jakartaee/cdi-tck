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
package org.jboss.jsr299.tck.tests.interceptors.definition.multipleBindings;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class MultipleInterceptorBindingsTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "9.5.1", id = "a"),
      @SpecAssertion(section = "9.5", id = "ca"),
      @SpecAssertion(section = "9.2", id = "ab"),
      @SpecAssertion(section = "9.3", id = "ba")
   })
   public void testInterceptorAppliedToBeanWithAllBindings()
   {
      MissileInterceptor.intercepted = false;
      
      Missile missile = getInstanceByType(FastAndDeadlyMissile.class);
      missile.fire();
      
      assert MissileInterceptor.intercepted;
   }
   
   @Test
   @SpecAssertion(section = "9.5.1", id = "b")
   public void testInterceptorNotAppliedToBeanWithSomeBindings()
   {
      MissileInterceptor.intercepted = false;
      
      Missile missile = getInstanceByType(SlowMissile.class);      
      missile.fire();
      
      assert !MissileInterceptor.intercepted;
   }
   
   @Test
   @SpecAssertion(section = "9.3", id = "bb")
   public void testMultipleInterceptorsOnMethod()
   {
      LockInterceptor.intercepted = false;
      
      GuidedMissile bullet = getInstanceByType(GuidedMissile.class);
      
      bullet.fire();
      assert !LockInterceptor.intercepted;
      
      bullet.lockAndFire();
      assert LockInterceptor.intercepted;
   }
  
}
