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
package org.jboss.jsr299.tck.tests.implementation.initializer;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class InitializerMethodTest extends AbstractJSR299Test
{

   @Test(groups = { "initializerMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "3.9.1", id = "f"),
      @SpecAssertion(section = "2.3.5", id = "b")
   })
   public void testBindingTypeOnInitializerParameter()
   {
      PremiumChickenHutch hutch = getInstanceByType(PremiumChickenHutch.class);
      assert hutch.getChicken().getName().equals("Preferred");
      StandardChickenHutch anotherHutch = getInstanceByType(StandardChickenHutch.class);
      assert anotherHutch.getChicken().getName().equals("Standard");
   }

   @Test(groups = { "initializerMethod" })
   @SpecAssertions({ 
      @SpecAssertion(section = "3.9", id = "g"), 
      @SpecAssertion(section = "3.9.1", id = "a"),
      @SpecAssertion(section = "3.9.1", id = "e"),
      @SpecAssertion(section = "5.5.2", id = "ad"),
      @SpecAssertion(section = "3.10", id = "a")
   })
   public void testMultipleInitializerMethodsAreCalled()
   {
      ChickenHutch chickenHutch = getInstanceByType(ChickenHutch.class);
      assert chickenHutch.fox != null;
      assert chickenHutch.chicken != null;
   }

}