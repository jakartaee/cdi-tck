package org.jboss.jsr299.tck.tests.extensions.beanManager;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

class Terrier extends Dog
{
   @Inject
   private InjectionPoint injectedMetadata;

   public InjectionPoint getInjectedMetadata()
   {
      return injectedMetadata;
   }
}
