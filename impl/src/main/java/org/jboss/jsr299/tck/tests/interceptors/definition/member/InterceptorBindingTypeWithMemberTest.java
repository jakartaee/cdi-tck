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
package org.jboss.jsr299.tck.tests.interceptors.definition.member;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * Tests for interceptor bindings types with members.
 * @author Jozef Hartinger
 *
 */
@Artifact
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class InterceptorBindingTypeWithMemberTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "9.5.2", id = "a")
   public void testInterceptorBindingTypeWithMember() {
      Farm farm = getInstanceByType(Farm.class);
      assert farm.getAnimalCount() == 20;
      assert IncreasingInterceptor.isIntercepted();
      assert !DecreasingInterceptor.isIntercepted();
   }
   
   @Test
   @SpecAssertion(section = "9.5.2", id = "b")
   public void testInterceptorBindingTypeWithNonBindingMember() {
      Farm farm = getInstanceByType(Farm.class);
      assert farm.getVehicleCount() == 20;
      assert VehicleCountInterceptor.isIntercepted();
   }
}
