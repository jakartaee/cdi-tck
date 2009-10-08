package org.jboss.jsr299.tck.spi;

import javax.enterprise.context.spi.Context;


/**
 * This interface provides operations relating to Contexts.
 * 
 * The TCK porting package must provide an implementation of this interface which is 
 * suitable for the target implementation. 
 * 
 * @author Shane Bryzak
 * @author Pete Muir
 * 
 * @param <T> The concrete context type of the implementation
 *
 */
public interface Contexts<T extends Context>
{
   
   public static final String PROPERTY_NAME = Contexts.class.getName();
   
   /**
    * Sets the specified context as active
    * 
    * @param context The context to set active
    */
   public void setActive(T context);
   
   /**
    * Sets the specified context as inactive
    * 
    * @param context The context to set inactive
    */
   public void setInactive(T context);
   
   /**
    * Get the request context, regardless of whether it is active or not
    * 
    * @return The request context
    */
   public T getRequestContext();
   
   /**
    * Returns the dependent context, regardless of whether it is active or not
    * 
    * @return the dependent context
    */
   public T getDependentContext();
   
   /**
    * Destroy the context. This operation is defined by the Web Beans
    * specification but has no API.
    * 
    * @param context the context to destroy
    */
   public void destroyContext(T context);
   
}
