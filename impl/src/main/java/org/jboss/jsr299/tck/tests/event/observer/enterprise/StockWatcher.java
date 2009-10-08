package org.jboss.jsr299.tck.tests.event.observer.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.event.Observes;

public @Stateful class StockWatcher implements StockWatcherLocal
{
   public void observeStockPrice(@Observes StockPrice price)
   {
      price.recordVisit(this);
   }

}
