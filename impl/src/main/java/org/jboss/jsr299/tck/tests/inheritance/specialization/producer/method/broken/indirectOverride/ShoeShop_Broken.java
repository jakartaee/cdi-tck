package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.indirectOverride;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;

class ShoeShop_Broken extends MallShop
{
   @Override @Produces @Specializes
   public Product getExpensiveGift()
   {
      return super.getExpensiveGift();
   }
   
}
