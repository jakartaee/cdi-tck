package org.jboss.jsr299.tck.tests.event.broken.observer.isDisposer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;

class FoxTerrier_Broken
{
   /* (non-Javadoc)
    * @see org.jboss.jsr299.tck.unit.event.broken.observer6.Terrier#observeInitialized(javax.inject.manager.Manager, java.lang.String)
    */
   public void observeInitialized(@Observes BeforeBeanDiscovery event, @Disposes String badParam)
   {
   }

}
