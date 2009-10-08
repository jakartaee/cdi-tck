package org.jboss.jsr299.tck.spi;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.testharness.api.DeploymentException;

/**
 * This interface provides operations relating to a Manager.
 * 
 * The TCK porting package must provide an implementation of this interface
 * which is suitable for the target implementation.
 * 
 * @author Shane Bryzak
 */
public interface Managers
{

   public static final String PROPERTY_NAME = Managers.class.getName();

   /**
    * Get a new Manager instance
    * 
    * @return the Manager
    */
   public BeanManager getManager();

   /**
    * Checks whether there is a definition error, as described in Section 12.4,
    * "Problems detected automatically by the container".
    * 
    * @param deploymentException the deployment exception context provided by
    *           the deployment which is in error. The deployment is not required
    *           to provide a deploymentException, even if the deployment is in
    *           error
    * @return true if there is a definition error
    */
   public boolean isDefinitionError(DeploymentException deploymentException);

   /**
    * Checks whether there is a deployment error, as described in Section 12.4,
    * "Problems detected automatically by the container".
    * 
    * @param deploymentException the deployment exception context provided by
    *           the deployment which is in error. The deployment is not required
    *           to provide a deploymentException, even if the deployment is in
    *           error
    * @return true if there is a deployment error
    */
   public boolean isDeploymentError(DeploymentException deploymentException);

}
