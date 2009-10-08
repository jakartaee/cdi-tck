package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.twoBeansSpecializeTheSameBean;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;

class Bookshop_Broken extends Shop
{
   @Override @Specializes @Produces
   public Product getExpensiveGift()
   {
      return new Product();
   }
   
}
