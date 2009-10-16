/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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

package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;


/**
 * Tests for the extensions provided by the ProcessAnnotatedType events.
 * 
 * @author David Allen
 * @author Jozef Hartinger
 *
 */
@Artifact
@BeansXml("beans.xml")
@Resources({
   @Resource(source="javax.enterprise.inject.spi.Extension", destination="WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
})
@IntegrationTest
@SpecVersion(spec="cdi", version="PFD2")
public class ProcessAnnotatedTypeTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.5", id = "a"),
      @SpecAssertion(section = "12.3", id = "ba")
   })
   public void testProcessAnnotatedTypeEventsSent()
   {
      // Randomly test some of the classes and interfaces that should have
      // been discovered and sent via the event
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(AbstractC.class);
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(ClassD.class);
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(Dog.class);
      assert ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(InterfaceA.class);
      //assert !ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(Tame.class);
   }
   
   @Test
   @SpecAssertion(section = "11.5.5", id = "ba")
   public void testGetAnnotatedType()
   {
      assert ProcessAnnotatedTypeObserver.getDogAnnotatedType().getBaseType().equals(Dog.class);
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.5", id = "bb"),
      @SpecAssertion(section = "11.5.5", id = "ca")
   })
   public void testSetAnnotatedType()
   {
      assert TestAnnotatedType.isGetConstructorsUsed();
      assert TestAnnotatedType.isGetFieldsUsed();
      assert TestAnnotatedType.isGetMethodsUsed();
   }
   
   @Test
   @SpecAssertion(section = "11.5.5", id = "bc")
   public void testVeto()
   {
      assert getCurrentManager().getBeans(VetoedBean.class).isEmpty();
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "11.4", id = "h")
   // WELD-200
   public void testGetBaseTypeUsedToDetermineTypeOfInjectionPoing() {
      // The base type of the fruit injection point is overridden to TropicalFruit
      assert GroceryAnnotatedType.isGetBaseTypeOfFruitFieldUsed();
      assert getInstanceByType(Grocery.class, new AnyLiteral()).getFruit().getMetadata().getType().equals(TropicalFruit.class);
   }
   
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "11.4", id = "k")
   // WELD-201
   public void testGetTypeClosureUsed() {
      assert GroceryAnnotatedType.isGetTypeClosureUsed();
      // should be [Object, Grocery] instead of [Object, Shop, Grocery]
      assert getBeans(Grocery.class, new AnyLiteral()).iterator().next().getTypes().size() == 2;
      assert getBeans(Shop.class, new AnyLiteral()).size() == 0;
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "l")
   public void testGetAnnotationUsedForGettingScopeInformation() {
      // @ApplicationScoped is overridden by @RequestScoped
      assert getBeans(Grocery.class, new AnyLiteral()).iterator().next().getScope().isAssignableFrom(RequestScoped.class);
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "m")
   public void testGetAnnotationUsedForGettingQualifierInformation() {
      // @Expensive is overridden by @Cheap
      assert getBeans(Grocery.class, new CheapLiteral()).size() == 1;
      assert getBeans(Grocery.class, new ExpensiveLiteral()).size() == 0;
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "n")
   public void testGetAnnotationUsedForGettingStereotypeInformation() {
      // The extension adds a stereotype with @Named qualifier
      assert getInstanceByName("grocery") != null;
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "p")
   public void testGetAnnotationUsedForGettingInterceptorInformation() {
      // The extension adds the GroceryInterceptorBinding
      assert getInstanceByType(Grocery.class, new AnyLiteral()).foo().equals("foo");
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "r")
   public void testPreviouslyNonInjectAnnotatedConstructorIsUsed() {
      assert getInstanceByType(Grocery.class, new AnyLiteral()).isConstructorWithParameterUsed();
   }
   
   @Test(groups="ri-broken")
   @SpecAssertion(section = "11.4", id = "t")
   public void testPreviouslyNonInjectAnnotatedFieldIsInjected() {
      assert getInstanceByType(Grocery.class, new AnyLiteral()).isVegetablesInjected();
   }
   
   @Test(groups="ri-broken")
   @SpecAssertion(section = "11.4", id = "u")
   public void testExtraQualifierIsAppliedToInjectedField() {
      Set<Annotation> qualifiers = getInstanceByType(Grocery.class, new AnyLiteral()).getFruit().getMetadata().getQualifiers();
      assert qualifiers.size() == 1;
      assert annotationSetMatches(qualifiers, Cheap.class);
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "v")
   public void testProducesCreatesProducerField() {
      // The extension adds @Producer to the bread field
      assert getBeans(Bread.class, new AnyLiteral()).size() == 1;
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "w")
   public void testInjectCreatesInitializerMethod() {
      // The extension adds @Inject to the nonInjectAnnotatedInitializer() method
      assert getInstanceByType(Grocery.class, new AnyLiteral()).isWaterInjected();
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "x")
   public void testQualifierAddedToInitializerParameter() {
      // The @Cheap qualifier is added to the method parameter
      Set<Annotation> qualifiers = getInstanceByType(Grocery.class, new AnyLiteral()).getInitializerFruit().getMetadata().getQualifiers();
      assert annotationSetMatches(qualifiers, Cheap.class);
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "y")
   public void testProducesCreatesProducerMethod() {
      // The extension adds @Producer to the getMilk() method
      assert getBeans(Milk.class, new AnyLiteral()).size() == 1;
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "z")
   public void testQualifierIsAppliedToProducerMethod() {
      // The extension adds @Expensive to the getMilk() method
      assert getBeans(Yogurt.class, new ExpensiveLiteral()).size() == 1;
      assert getBeans(Yogurt.class, new CheapLiteral()).size() == 0;
   }
   
   @Test
   @SpecAssertion(section = "11.4", id = "aa")
   public void testQualifierIsAppliedToProducerMethodParameter() {
      // The @Cheap qualifier is added to the method parameter
      Set<Annotation> qualifiers = getInstanceByType(Yogurt.class, new AnyLiteral()).getFruit().getMetadata().getQualifiers();
      assert qualifiers.size() == 1;
      assert annotationSetMatches(qualifiers, Cheap.class);
   }
}
