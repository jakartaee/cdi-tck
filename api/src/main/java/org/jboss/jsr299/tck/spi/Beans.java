package org.jboss.jsr299.tck.spi;

/**
 * Provides Bean related operations.
 * 
 * The TCK porting package must provide an implementation of this interface which is 
 * suitable for the target implementation.
 * 
 * This interface may be removed.
 * 
 * @author Shane Bryzak
 * @author Pete Muir
 * @author David Allen
 * 
 */
public interface Beans
{

   public static final String PROPERTY_NAME = Beans.class.getName();
   
   /**
    * Determines if the object instance is actually a proxy object.
    * 
    * @param instance The object which might be a proxy
    * @return true if the object is a proxy
    */
   public boolean isProxy(Object instance);
   
}
