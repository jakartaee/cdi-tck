package org.jboss.jsr299.tck.tests.event.observer;

import javax.enterprise.event.Observes;

class StockWatcher
{
   private static Class<?> observerClazz;
   
   public void observeStockPrice(@Observes StockPrice price)
   {
      observerClazz = this.getClass();
      price.recordVisit(this);
   }

   public static Class<?> getObserverClazz()
   {
      return observerClazz;
   }
}
