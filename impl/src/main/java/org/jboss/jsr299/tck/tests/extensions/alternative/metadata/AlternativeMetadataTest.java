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
package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test class contains tests for adding meta data using extensions.
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class AlternativeMetadataTest extends AbstractJSR299Test
{
    
    @Deployment
    public static WebArchive createTestArchive() 
	{
        return new WebArchiveBuilder()
            .withTestClassPackage(AlternativeMetadataTest.class)
            .withBeansXml("beans.xml")
            .withExtension("javax.enterprise.inject.spi.Extension")
            .build();
    }
    
   @Test
   @SpecAssertion(section = "11.4", id = "ha")
   public void testGetBaseTypeUsedToDetermineTypeOfInjectionPoint()
   {
      // The base type of the fruit injection point is overridden to
      // TropicalFruit
      assert GroceryWrapper.isGetBaseTypeOfFruitFieldUsed();
      assert getInstanceByType(Grocery.class, new AnyLiteral()).getFruit().getMetadata().getType().equals(TropicalFruit.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "ka")
   public void testGetTypeClosureUsed()
   {
      assert GroceryWrapper.isGetTypeClosureUsed();
      // should be [Object, Grocery] instead of [Object, Shop, Grocery]
      assert getBeans(Grocery.class, new AnyLiteral()).iterator().next().getTypes().size() == 2;
      assert getBeans(Shop.class, new AnyLiteral()).size() == 0;
   }

   @Test
   @SpecAssertion(section = "11.4", id = "l")
   public void testGetAnnotationUsedForGettingScopeInformation()
   {
      // @ApplicationScoped is overridden by @RequestScoped
      assert getBeans(Grocery.class, new AnyLiteral()).iterator().next().getScope().equals(RequestScoped.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "m")
   public void testGetAnnotationUsedForGettingQualifierInformation()
   {
      // @Expensive is overridden by @Cheap
      assert getBeans(Grocery.class, new CheapLiteral()).size() == 1;
      assert getBeans(Grocery.class, new ExpensiveLiteral()).size() == 0;
   }

   @Test
   @SpecAssertion(section = "11.4", id = "n")
   public void testGetAnnotationUsedForGettingStereotypeInformation()
   {
      // The extension adds a stereotype with @Named qualifier
      assert getInstanceByName("grocery") != null;
   }

   @Test
   @SpecAssertion(section = "11.4", id = "p")
   public void testGetAnnotationUsedForGettingInterceptorInformation()
   {
      // The extension adds the GroceryInterceptorBinding
      Grocery grocery = getInstanceByType(Grocery.class, new AnyLiteral());
      assert grocery.foo().equals("foo");
   }

   @Test
   @SpecAssertion(section = "11.4", id = "r")
   public void testPreviouslyNonInjectAnnotatedConstructorIsUsed()
   {
      assert getInstanceByType(Grocery.class, new AnyLiteral()).isConstructorWithParameterUsed();
   }

   @Test
   @SpecAssertion(section = "11.4", id = "t")
   public void testPreviouslyNonInjectAnnotatedFieldIsInjected()
   {
      assert getInstanceByType(Grocery.class, new AnyLiteral()).isVegetablesInjected();
   }

   @SuppressWarnings("unchecked")
   @SpecAssertion(section = "11.4", id = "u")
   public void testExtraQualifierIsAppliedToInjectedField()
   {
      assert getInstanceByType(Grocery.class, new AnyLiteral()).getFruit() != null;
      Set<Annotation> qualifiers = getInstanceByType(Grocery.class, new AnyLiteral()).getFruit().getMetadata().getQualifiers();
      assert qualifiers.size() == 1;
      assert annotationSetMatches(qualifiers, Cheap.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "v")
   public void testProducesCreatesProducerField()
   {
      // The extension adds @Producer to the bread field
      assert getBeans(Bread.class, new AnyLiteral()).size() == 1;
   }

   @Test
   @SpecAssertion(section = "11.4", id = "w")
   public void testInjectCreatesInitializerMethod()
   {
      // The extension adds @Inject to the nonInjectAnnotatedInitializer()
      // method
      assert getInstanceByType(Grocery.class, new AnyLiteral()).isWaterInjected();
   }

   @SuppressWarnings("unchecked")
   @Test
   @SpecAssertion(section = "11.4", id = "x")
   public void testQualifierAddedToInitializerParameter()
   {
      // The @Cheap qualifier is added to the method parameter
      Set<Annotation> qualifiers = getInstanceByType(Grocery.class, new AnyLiteral()).getInitializerFruit().getMetadata().getQualifiers();
      assert annotationSetMatches(qualifiers, Cheap.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "y")
   public void testProducesCreatesProducerMethod()
   {
      // The extension adds @Producer to the getMilk() method
      assert getBeans(Milk.class, new AnyLiteral()).size() == 1;
   }

   @Test
   @SpecAssertion(section = "11.4", id = "z")
   public void testQualifierIsAppliedToProducerMethod()
   {
      // The extension adds @Expensive to the getMilk() method
      assert getBeans(Yogurt.class, new ExpensiveLiteral()).size() == 1;
      assert getBeans(Yogurt.class, new CheapLiteral()).size() == 0;
   }

   @SuppressWarnings("unchecked")
   @Test
   @SpecAssertion(section = "11.4", id = "aa")
   public void testQualifierIsAppliedToProducerMethodParameter()
   {
      // The @Cheap qualifier is added to the method parameter
      Set<Annotation> qualifiers = getInstanceByType(Yogurt.class, new AnyLiteral()).getFruit().getMetadata().getQualifiers();
      assert qualifiers.size() == 1;
      assert annotationSetMatches(qualifiers, Cheap.class);
   }

   @SuppressWarnings("unchecked")
   @Test
   @SpecAssertions( { @SpecAssertion(section = "11.4", id = "ae"), @SpecAssertion(section = "11.4", id = "ag") })
   public void testObserverMethod()
   {
      getCurrentManager().fireEvent(new Milk(true));
      Milk event = getInstanceByType(Grocery.class, new AnyLiteral()).getObserverEvent();
      TropicalFruit parameter = getInstanceByType(Grocery.class, new AnyLiteral()).getObserverParameter();
      assert event != null;
      assert parameter != null;
      assert parameter.getMetadata().getQualifiers().size() == 1;
      assert annotationSetMatches(parameter.getMetadata().getQualifiers(), Cheap.class);
   }

   @Test
   @SpecAssertion(section = "11.4", id = "af")
   public void testExtraQualifierAppliedToObservesMethodParameter()
   {
      getCurrentManager().fireEvent(new Bread(true));
      // normally, the event would be observer, however the extension adds the
      // @Expensive qualifier to the method parameter
      assert !getInstanceByType(Grocery.class, new AnyLiteral()).isObserver2Used();
   }
}
