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
package org.jboss.jsr299.tck.tests.lookup.dynamic;


import java.util.Iterator;

import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for dynamic lookup features
 * 
 * @author Shane Bryzak
 * @author Jozef Hartinger
 */
@SpecVersion(spec="cdi", version="20091101")
public class DynamicLookupTest extends AbstractJSR299Test
{
    
    @Deployment
   public static WebArchive createTestArchive() 
	{
       return new WebArchiveBuilder()
           .withTestClassPackage(DynamicLookupTest.class)
           .build();
   }
    
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id ="aa")
   })
   public void testObtainsInjectsInstanceOfInstance()
   {
      ObtainsInstanceBean injectionPoint = getInstanceByType(ObtainsInstanceBean.class);
      assert injectionPoint.getPaymentProcessor() != null;
   }
   
   @Test
   @SpecAssertion(section = "5.6.1", id ="da")
   public void testDuplicateBindingsThrowsException()
   {
       try
       {
           ObtainsInstanceBean injectionPoint = getInstanceByType(ObtainsInstanceBean.class);
           injectionPoint.getAnyPaymentProcessor().select(new DefaultLiteral(), new DefaultLiteral());
       }
       catch (Throwable t) 
       {
         assert isThrowablePresent(IllegalArgumentException.class, t);
         return;
      }
       assert false;
   }      
   
   @Test
   @SpecAssertion(section = "5.6.1", id = "e")
   public void testNonBindingThrowsException()
   {
       try
       {
           ObtainsInstanceBean injectionPoint = getInstanceByType(ObtainsInstanceBean.class);
           injectionPoint.getAnyPaymentProcessor().select(new AnnotationLiteral<NonBinding>(){});      
       }
       catch (Throwable t) 
       {
         assert isThrowablePresent(IllegalArgumentException.class, t);
         return;
      }
       assert false;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id="ba"),
      @SpecAssertion(section = "5.6", id ="ca"),
      @SpecAssertion(section = "5.6.1", id ="aa"),
      @SpecAssertion(section = "5.6.1", id ="ab"),
      @SpecAssertion(section = "5.6.1", id="fa"),
      @SpecAssertion(section = "5.6.1", id="fc")
   })
   public void testGetMethod() {
      // initial setup of contextual instance
      getInstanceByType(AdvancedPaymentProcessor.class, new AnyLiteral()).setValue(10);
      
      Instance<AsynchronousPaymentProcessor> instance = getInstanceByType(ObtainsInstanceBean.class).getPaymentProcessor();
      assert instance.get() instanceof AdvancedPaymentProcessor;
      assert instance.get().getValue() == 10;
   }
   
   @Test
   @SpecAssertion(section = "5.6.1", id = "fba")
   public void testUnsatisfiedDependencyThrowsException()
   {
       try
       {
           getInstanceByType(ObtainsInstanceBean.class).getPaymentProcessor().select(RemotePaymentProcessor.class).get();
       }
       catch (Throwable t) 
       {
         assert isThrowablePresent(UnsatisfiedResolutionException.class, t);
         return;
      }
       assert false;
   }
   
   @Test
   @SpecAssertion(section = "5.6.1", id = "fbb")
   public void testAmbiguousDependencyThrowsException()
   {
       try
       {      
           getInstanceByType(ObtainsInstanceBean.class).getAnyPaymentProcessor().get();
       }
       catch (Throwable t) 
       {
         assert isThrowablePresent(AmbiguousResolutionException.class, t);
         return;
      }
       assert false;
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="5.6.1", id="aa"),
      @SpecAssertion(section="5.6.1", id="ba"),
      @SpecAssertion(section="5.6.1", id="ja"),
      @SpecAssertion(section="5.6.1", id="ka"),
      @SpecAssertion(section="5.6.3", id="a")
   })
   public void testIteratorMethod()
   {
      // initial setup of contextual instances
      getInstanceByType(AdvancedPaymentProcessor.class, new AnyLiteral()).setValue(1);
      getInstanceByType(RemotePaymentProcessor.class, new AnyLiteral()).setValue(2);

      Instance<PaymentProcessor> instance = getInstanceByType(ObtainsInstanceBean.class).getAnyPaymentProcessor();
      Iterator<AsynchronousPaymentProcessor> iterator1 = instance.select(AsynchronousPaymentProcessor.class).iterator();

      AdvancedPaymentProcessor advanced = null;
      RemotePaymentProcessor remote = null;
      int instances = 0;
      while (iterator1.hasNext())
      {
         PaymentProcessor processor = iterator1.next();
         if (processor instanceof AdvancedPaymentProcessor)
         {
            advanced = (AdvancedPaymentProcessor) processor;
         }
         else if (processor instanceof RemotePaymentProcessor)
         {
            remote = (RemotePaymentProcessor) processor;
         }
         else
         {
            throw new RuntimeException("Unexpected instance returned by iterator.");
         }
         instances++;
      }

      assert instances == 2;
      assert advanced != null;
      assert advanced.getValue() == 1;
      assert remote != null;
      assert remote.getValue() == 2;

      Iterator<RemotePaymentProcessor> iterator2 = instance.select(RemotePaymentProcessor.class, new PayByBinding()
      {
         public PaymentMethod value()
         {
            return PaymentMethod.CREDIT_CARD;
         }
      }).iterator();

      assert iterator2.next().getValue() == 2;
      assert !iterator2.hasNext();
   }
   
   @Test
   @SpecAssertion(section = "5.6.1", id = "l")
   public void testIsUnsatisfied()
   {
      ObtainsInstanceBean injectionPoint = getInstanceByType(ObtainsInstanceBean.class);
      assert !injectionPoint.getAnyPaymentProcessor().isUnsatisfied();
      assert injectionPoint.getPaymentProcessor().select(RemotePaymentProcessor.class).isUnsatisfied();
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id = "da"),
      @SpecAssertion(section = "5.6.1", id = "m")
   })
   public void testIsAmbiguous()
   {
      ObtainsInstanceBean injectionPoint = getInstanceByType(ObtainsInstanceBean.class);
      assert injectionPoint.getAnyPaymentProcessor().isAmbiguous();
      assert !injectionPoint.getPaymentProcessor().isAmbiguous();
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "5.6", id = "e"),
      @SpecAssertion(section = "5.6.3", id = "b")
   })
   @SuppressWarnings("serial")
   public void testNewBean()
   {
       // TODO check possible weld bug  
       // Instance<List<String>> instance = getInstanceByType(ObtainsNewInstanceBean.class).getStrings();
       // assert instance.select(new TypeLiteral<ArrayList<String>>(){}).get() instanceof ArrayList<?>;
       String instance = getInstanceByType(ObtainsNewInstanceBean.class).getString();
       assert instance != null && instance instanceof String;
       getInstanceByType(ObtainsNewInstanceBean.class).getMap();
   }
   
   @Test
   @SpecAssertion(section = "3.12", id = "xc")
   public void testNewBeanNotEnabledWithouInjectionPoint()
   {
      assert getInstanceByType(ObtainsNewInstanceBean.class).getIae().isUnsatisfied();
   }
}
