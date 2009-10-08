package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.inject.Initializer;
import javax.inject.Produces;
import javax.inject.Production;


@Production
class Duck
{
   
   @Produces public static String foo = "foo";
   
   @Produces public static Integer bar = 1;
   
   @Produces @Synchronous public static Integer synchronousBar = 2;
   
   public static boolean constructedCorrectly = false;
   
   @Initializer
   public Duck(String foo, @Synchronous Integer bar)
   {
      if (foo.equals(Duck.foo) && bar.equals(Duck.synchronousBar))
      {
         constructedCorrectly = true;
      }
   }
   
}
