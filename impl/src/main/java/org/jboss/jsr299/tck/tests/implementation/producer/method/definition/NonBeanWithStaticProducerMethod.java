package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class NonBeanWithStaticProducerMethod
{
   public NonBeanWithStaticProducerMethod(String someString)
   {
      
   }

   @Produces public static Cherry getCherry()
   {
      return new Cherry("Marischino");
   }
   
   public static void destroyString(@Disposes Cherry someCherry)
   {
   }
}
