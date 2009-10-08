package org.jboss.jsr299.tck.tests.event.observer.enterprise;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

public @Local interface StockWatcherLocal
{
   void observeStockPrice(@Observes StockPrice price);
}
