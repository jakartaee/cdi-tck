package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.annotation.PreDestroy;

class Salmon
{
   private static boolean beanDestroyed = false;
   
   public Salmon()
   {
      beanDestroyed = false;
   }
   
   @PreDestroy
   public void destroy()
   {
      beanDestroyed = true;
   }

   public static boolean isBeanDestroyed()
   {
      return beanDestroyed;
   }
}
