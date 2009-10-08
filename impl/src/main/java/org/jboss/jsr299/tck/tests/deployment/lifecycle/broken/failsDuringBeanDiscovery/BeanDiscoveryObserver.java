package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.failsDuringBeanDiscovery;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;

class BeanDiscoveryObserver
{
   public void afterBeanDiscovery(@Observes AfterBeanDiscovery event)
   {
      assert false : "Deployment should have already failed because of the presence of an invalid @Inject method";
   }
}
