package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

class Turkey
{
   
   @Produces public static String foo = "foo";
   
   @Produces static Integer bar = 1;
   
   public static boolean constructedCorrectly = false;
   
   public Turkey()
   {
      
   }
   
   @Inject
   public Turkey(String foo, Integer bar)
   {
      if (foo.equals(Turkey.foo) && bar.equals(Turkey.bar))
      {
         constructedCorrectly = true;
      }
   }

}
