package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.exceptionInAfterBeanValidationObserver;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;

class BeanDiscoveryObserver
{
   public void afterDeploymentValidation(@Observes AfterDeploymentValidation event)
   {
      throw new AssertionError("This error should be treated as a deployment error");
   }
}
