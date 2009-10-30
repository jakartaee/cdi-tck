package org.jboss.jsr299.tck.tests.interceptors.definition.custom;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;


public class AfterBeanDiscoveryObserver implements Extension
{
   public void addInterceptors(@Observes AfterBeanDiscovery event) {
      event.addBean(new CustomInterceptorImplementation());
   }

}
