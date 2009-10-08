package org.jboss.jsr299.tck.tests.event.broken.observer.tooManyParameters;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;

class YorkshireTerrier_Broken
{
   public void observesAfterBeanDiscovery(@Observes AfterBeanDiscovery beforeBeanDiscovery, @Observes Boxer anotherDog)
   {
   }
}
