package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


class Shop
{
   
   @Produces @Expensive @Named
   public Necklace getExpensiveGift()
   {
      return new Necklace();
   }
   
}
