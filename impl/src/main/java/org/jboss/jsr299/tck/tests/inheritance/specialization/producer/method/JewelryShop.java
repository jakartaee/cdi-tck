package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;

class JewelryShop extends Shop
{
   @Override @Produces @Specializes @Sparkly
   public Necklace getExpensiveGift()
   {
      return new Necklace();
   }
   
}
