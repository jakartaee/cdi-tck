package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.exceptionInAfterBeanDiscoveryObserver;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class BeanDiscoveryObserver implements Extension
{
   public void afterBeanDiscovery(@Observes AfterBeanDiscovery event)
   {
      throw new FooException("This error should be treated as a definition error");
   }
}
