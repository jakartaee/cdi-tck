package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.addDeploymentProblem;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Extension;

public class BeanDiscoveryObserver implements Extension
{
   private static int invocationCount = 0;
   
   public void afterDeploymentValidation(@Observes AfterDeploymentValidation event)
   {
      event.addDeploymentProblem(new AssertionError("This error should be treated as a deployment error"));
      invocationCount++;
   }
   
   public void alsoAfterDeploymentValidation(@Observes AfterDeploymentValidation event)
   {
      event.addDeploymentProblem(new AssertionError("This error should also be treated as a deployment error"));
      invocationCount++;
   }
   
   public static int getInvocationCount()
   {
      return invocationCount;
   }
}
