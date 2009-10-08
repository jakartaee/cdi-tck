package org.jboss.jsr299.tck.tests.event.broken.observer.isProducer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AfterBeanDiscovery;

class BorderTerrier_Broken
{
   public @Produces String observesAfterBeanDiscovery(@Observes AfterBeanDiscovery event)
   {
      return "product";
   }
}
