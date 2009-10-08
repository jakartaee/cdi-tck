package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

class Sheep
{
   
   public static boolean constructedCorrectly = false;
   
   @Produces @ClovenHoved
   public static String foo = "foo";
   
   @Produces @ClovenHoved public static Double bar = 5.5;
   
   @Inject
   public Sheep(@ClovenHoved String foo, @ClovenHoved Double bar)
   {
      if (foo.equals(Sheep.foo) && bar.equals(Sheep.bar))
      {
         constructedCorrectly = true;
      }
   }
   
}
