package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.addDefinitionError;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class BeanDiscoveryObserver implements Extension
{
   private static int invocationCount = 0;
   
   public void afterBeanDiscovery(@Observes AfterBeanDiscovery event)
   {
      event.addDefinitionError(new AssertionError("This error should be treated as a definition error"));
      invocationCount++;
   }
   
   public void alsoAfterBeanDiscovery(@Observes AfterBeanDiscovery event)
   {
      event.addDefinitionError(new AssertionError("This error should also be treated as a definition error"));
      invocationCount++;
   }
   
   public static int getInvocationCount()
   {
      return invocationCount;
   }
}
