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
package org.jboss.jsr299.tck.tests.lookup.circular;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class CircularDependencyTest extends AbstractJSR299Test
{
  
   @Test
   @SpecAssertion(section="5", id="b")
   public void testCircularInjectionOnTwoNormalBeans() throws Exception
   {
      Pig pig = getInstanceByType(Pig.class);
      Food food = getInstanceByType(Food.class);
      assert pig.getNameOfFood().equals(food.getName());
      assert food.getNameOfPig().equals(pig.getName());
   }
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testCircularInjectionOnOneNormalAndOneDependentBean() throws Exception
   {
      Petrol petrol = getInstanceByType(Petrol.class);
      Car car = getInstanceByType(Car.class);
      assert petrol.getNameOfCar().equals(car.getName());
      assert car.getNameOfPetrol().equals(petrol.getName());
   }
   
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testNormalProducerMethodDeclaredOnNormalBeanWhichInjectsProducedBean() throws Exception
   {
      getInstanceByType(NormalSelfConsumingNormalProducer.class).ping();
   }
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testNormalProducerMethodDeclaredOnDependentBeanWhichInjectsProducedBean() throws Exception
   {
      getInstanceByType(DependentSelfConsumingNormalProducer.class).ping();
   }
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testDependentProducerMethodDeclaredOnNormalBeanWhichInjectsProducedBean() throws Exception
   {
      getInstanceByType(NormalSelfConsumingDependentProducer.class).ping();
   }
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testNormalSelfConsumingProducer() throws Exception
   {
      getInstanceByType(Violation.class).ping();
   }
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testNormalCircularConstructors() throws Exception
   {
      assert getInstanceByType(Bird.class) != null;
   }
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testNormalAndDependentCircularConstructors() throws Exception
   {      
      assert getInstanceByType(Planet.class) != null;
   }
   
   @Test
   @SpecAssertion(section="5", id="b")
   public void testSelfConsumingConstructorsOnNormalBean() throws Exception
   {      
      assert getInstanceByType(House.class) != null;
   }
}
