package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.twoBeansSpecializeTheSameBean;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;

class PictureShop_Broken extends Shop
{
   @Override @Produces @Specializes
   public Product getExpensiveGift()
   {
      return super.getExpensiveGift();
   }
   
}
