package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.event.Observes;

class Shop
{
   
   public static String deliveryObservedBy = null;
   
   public String getClassName()
   {
      return Shop.class.getName();
   }
   
   public void observeDelivery(@Observes Delivery delivery)
   {
      deliveryObservedBy = getClassName();
   }
   
}
