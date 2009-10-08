package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.exceptionInAfterBeanDiscoveryObserver;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;

class BeanDiscoveryObserver
{
   public void afterBeanDiscovery(@Observes AfterBeanDiscovery event)
   {
      throw new FooException("This error should be treated as a definition error");
   }
}
