package org.jboss.jsr299.tck.spi;

import javax.enterprise.inject.spi.BeanManager;

/**
 * This interface provides operations relating to a Manager.
 * 
 * The TCK porting package must provide an implementation of this interface
 * which is suitable for the target implementation.
 * 
 * Managers also allows the TCK to report the state of the Container back to the
 * TCK, by checking if a deployment problem has occurred, 
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

}
