package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.broken.methodOnSessionBean;

import javax.ejb.Stateless;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

@Stateless
public class AppleTree implements AppleTreeLocal
{
   @Produces public Apple getApple()
   {
      return new Apple();
   }
   
   public void recycle(@Disposes Apple apple)
   {
      
   }
  
}
