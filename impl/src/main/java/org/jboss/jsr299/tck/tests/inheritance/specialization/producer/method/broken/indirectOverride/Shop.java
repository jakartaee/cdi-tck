package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.indirectOverride;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


class Shop
{
   
   @Produces @Expensive @Named
   public Product getExpensiveGift()
   {
      return new Product();
   }
   
}
