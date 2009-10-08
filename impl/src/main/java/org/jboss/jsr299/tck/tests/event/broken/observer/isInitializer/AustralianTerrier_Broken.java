package org.jboss.jsr299.tck.tests.event.broken.observer.isInitializer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.inject.Inject;

class AustralianTerrier_Broken
{
   public @Inject void observesAfterBeanDiscovery(@Observes AfterBeanDiscovery discovery)
   {
   }
}
