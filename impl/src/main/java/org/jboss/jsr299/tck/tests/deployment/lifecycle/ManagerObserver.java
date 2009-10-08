package org.jboss.jsr299.tck.tests.deployment.lifecycle;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

public class ManagerObserver implements Extension
{
   private static boolean afterBeanDiscoveryCalled = false;
   private static boolean afterDeploymentValidationCalled = false;
   
   public void managerInitialized(@Observes AfterBeanDiscovery event, BeanManager beanManager)
   {
      afterBeanDiscoveryCalled = true;
   }

   public void managerDeployed(@Observes AfterDeploymentValidation event, BeanManager beanManager)   {
      assert afterBeanDiscoveryCalled : "AfterBeanDiscovery should have been called before AfterDeploymentValidation";
      afterDeploymentValidationCalled = true;
   }

   public static boolean isAfterBeanDiscoveryCalled()
   {
      return afterBeanDiscoveryCalled;
   }

   public static void reset()
   {
      ManagerObserver.afterBeanDiscoveryCalled = false;
      ManagerObserver.afterDeploymentValidationCalled = false;
   }

   public static boolean isAfterDeploymentValidationCalled()
   {
      return afterDeploymentValidationCalled;
   }

}
