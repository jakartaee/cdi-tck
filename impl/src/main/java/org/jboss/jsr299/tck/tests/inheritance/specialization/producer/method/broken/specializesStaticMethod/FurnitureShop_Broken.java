package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.specializesStaticMethod;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;

class FurnitureShop_Broken extends Shop
{
   @Specializes @Produces 
   public static Product getChair()
   {
      return new Product();
   }
   
}
