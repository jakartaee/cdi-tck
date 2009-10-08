package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class BeanWithStaticProducerMethod
{
   static boolean stringDestroyed;

   @Produces public static String getString()
   {
      stringDestroyed = false;
      return "Pete";
   }
   
   public static void destroyString(@Disposes String someString)
   {
      stringDestroyed = true;
   }
}
