package org.jboss.jsr299.tck.tests.context.session;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;

@SessionScoped
class SimpleSessionBean implements Serializable
{

   private static final long serialVersionUID = 1L;
   private static boolean beanDestroyed = false;
   
   @PreDestroy
   public void destroyBean()
   {
      beanDestroyed = true;
   }

   public static boolean isBeanDestroyed()
   {
      return beanDestroyed;
   }

   public static void setBeanDestroyed(boolean beanDestroyed)
   {
      SimpleSessionBean.beanDestroyed = beanDestroyed;
   }
}
