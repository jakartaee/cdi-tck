package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.failsDuringValidation;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;

class BeanDiscoveryObserver
{
   private static boolean afterBeanDiscovery;
   
   public void afterBeanDiscovery(@Observes AfterBeanDiscovery event)
   {
      afterBeanDiscovery = true;
   }

   public void afterDeploymentValidation(@Observes AfterDeploymentValidation event)
   {
      assert false : "Deployment should have already failed because of the presence of an unsatisfied dependency";
   }

   public static boolean isAfterBeanDiscovery()
   {
      return afterBeanDiscovery;
   }

   public static void setAfterBeanDiscovery(boolean managerInitialized)
   {
      BeanDiscoveryObserver.afterBeanDiscovery = managerInitialized;
   }
}
