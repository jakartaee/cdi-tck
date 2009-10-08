package org.jboss.jsr299.tck.spi;

import javax.el.ELContext;

/**
 * This interface provides operations relating to EL.
 * 
 * The TCK porting package must provide an implementation of this interface which is 
 * suitable for the target Web Beans implementation. 
 * 
 * @author Pete Muir
 */
public interface EL
{
   
   public static final String PROPERTY_NAME  = EL.class.getName();
   
   /**
    * 
    * 
    * @param <T>
    * @param expression
    * @param expectedType
    * @return
    */
   public <T> T evaluateValueExpression(String expression, Class<T> expectedType);
   
   /**
    * 
    * @param <T>
    * @param expression
    * @param expectedType
    * @param expectedParamTypes
    * @return
    */
   public <T> T evaluateMethodExpression(String expression, Class<T> expectedType, Class<?>[] expectedParamTypes, Object[] expectedParams);
   
   public ELContext createELContext();
   
}
