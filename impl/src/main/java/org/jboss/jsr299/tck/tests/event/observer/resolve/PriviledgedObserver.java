package org.jboss.jsr299.tck.tests.event.observer.resolve;

import javax.enterprise.event.Observes;

class PriviledgedObserver
{
   public void observeSecret(@Observes @Secret String secretString)
   {
   }
}
