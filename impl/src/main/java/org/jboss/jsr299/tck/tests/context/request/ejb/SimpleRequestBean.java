package org.jboss.jsr299.tck.tests.context.request.ejb;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;

@RequestScoped
class SimpleRequestBean
{
   private double id = Math.random();
   private static boolean beanDestroyed = false;

   public double getId()
   {
      return id;
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

   public static void setBeanDestroyed(boolean beanDestroyed)
   {
      SimpleRequestBean.beanDestroyed = beanDestroyed;
   }
}
