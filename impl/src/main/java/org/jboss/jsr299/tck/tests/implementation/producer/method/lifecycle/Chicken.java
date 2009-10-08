package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.inject.Produces;

class Chicken
{
   private static Egg egg = new Egg();
   
   @Produces
   public Egg produceEgg()
   {
      return getEgg();
   }

   public Egg getEgg()
   {
      return egg;
   }
}
