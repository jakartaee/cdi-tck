package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.useBeforeValidationFails;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;

class ManagerObserver
{
   private static boolean managerInitialized = false;
   private static boolean managerDeployed = false;
   
   public void managerInitialized(@Observes AfterBeanDiscovery event, BeanManager beanManager)
   {
      managerInitialized = true;
      beanManager.fireEvent("An event that should not be fired");
   }

   public void managerDeployed(@Observes AfterDeploymentValidation event, BeanManager beanManager)   {
      managerDeployed = true;
   }

   public static boolean isManagerInitialized()
   {
      return managerInitialized;
   }

   public static void setManagerInitialized(boolean managerInitialized)
   {
      ManagerObserver.managerInitialized = managerInitialized;
   }

   public static boolean isManagerDeployed()
   {
      return managerDeployed;
   }

   public static void setManagerDeployed(boolean managerDeployed)
   {
      ManagerObserver.managerDeployed = managerDeployed;
   }
}
