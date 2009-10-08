package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

class StockWatcher
{
   private static Class<?> observerClazz;
   private static boolean  anyVolumeObserved = false;
   
   public void observeStockPrice(@Observes StockPrice price)
   {
      observerClazz = this.getClass();
      price.recordVisit(this);
   }

   public void observeAllVolume(@Observes @Any Volume volume)
   {
      anyVolumeObserved = true;
   }

   public static Class<?> getObserverClazz()
   {
      return observerClazz;
   }

   public static boolean isAnyVolumeObserved()
   {
      return anyVolumeObserved;
   }

   public static void setAnyVolumeObserved(boolean anyStockPriceObserved)
   {
      StockWatcher.anyVolumeObserved = anyStockPriceObserved;
   }
}
