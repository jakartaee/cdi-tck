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
package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.implementation.simple.definition.OuterClass.InnerClass_NotBean;
import org.jboss.jsr299.tck.tests.implementation.simple.definition.OuterClass.StaticInnerClass;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@Resources({ 
   @Resource(destination="/WEB-INF/faces-config.xml", source="faces-config.xml")
})
public class SimpleBeanDefinitionTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertion(section = "3.1.1", id = "ca")
   public void testAbstractClassDeclaredInJavaNotDiscovered()
   {
      assert getBeans(Cow_NotBean.class).size() == 0;
   }

   @Test(groups = "innerClass")
   @SpecAssertions({
      @SpecAssertion(section = "3.1.1", id = "ba")
   })
   public void testStaticInnerClassDeclaredInJavaAllowed()
   {
      assert getBeans(StaticInnerClass.class).size() == 1;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.1.1", id = "b")
   })
   public void testNonStaticInnerClassDeclaredInJavaNotDiscovered()
   {
      assert getBeans(InnerClass_NotBean.class).size() == 0;
   }

   @Test
   @SpecAssertion(section = "3.1.1", id = "cb")
   public void testInterfaceNotDiscoveredAsSimpleBean()
   {
      assert getBeans(Car.class).size() == 0;
   }
   
   @Test
   @SpecAssertion(section = "3.1.1", id = "g")
   public void testExtensionNotDiscoveredAsSimpleBean()
   {
      assert getBeans(SimpleExtension.class).size() == 0;
   }

   @Test
   @SpecAssertion(section="3.1.1", id="p")
   public void testSimpleBeanOnlyIfConstructorParameterless()
   {
      assert getBeans(Antelope_NotBean.class).isEmpty();
      assert !getBeans(Donkey.class).isEmpty();
   }

   @Test
   @SpecAssertion(section="3.1.1", id="q")
   public void testSimpleBeanOnlyIfConstructorIsInitializer()
   {
      assert getBeans(Antelope_NotBean.class).isEmpty();
      assert !getBeans(Sheep.class).isEmpty();
   }

   @Test
   @SpecAssertion(section = "3.7.1", id = "aa")
   public void testInitializerAnnotatedConstructor() throws Exception
   {
      Sheep.constructedCorrectly = false;
      getInstanceByType(Sheep.class);
      assert Sheep.constructedCorrectly;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.7.1", id = "ba"),
      @SpecAssertion(section = "3.1.3", id = "a"),
      @SpecAssertion(section = "3.7", id = "a"),
      @SpecAssertion(section = "5.5.1", id = "ba")
   })
   public void testEmptyConstructorUsed()
   {
      Donkey.constructedCorrectly = false;
      getInstanceByType(Donkey.class);
      assert Donkey.constructedCorrectly;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.7.1", id = "aa"),
      @SpecAssertion(section = "5.5.1", id = "aa")
   })
   public void testInitializerAnnotatedConstructorUsedOverEmptyConstuctor() throws Exception
   {
      getInstanceByType(Turkey.class);
      assert Turkey.constructedCorrectly;
   }

   @Test
   @SpecAssertion(section = "3.1", id = "fa")
   public void testDependentScopedBeanCanHavePublicField() throws Exception
   {
      assert getInstanceByType(Tiger.class).name.equals("pete");
   }
}
