package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

@Named("foxRun") @Default
class FoxRun
{   
   private static boolean destroyed = false;

   @Inject
   public Fox fox;
   
   @Inject
   public Fox anotherFox;
   
   @PreDestroy
   public void destroy()
   {
      destroyed = true;
   }
   
   public static void setDestroyed(boolean destroyed)
   {
      FoxRun.destroyed = destroyed;
   }
   
   public static boolean isDestroyed()
   {
      return destroyed;
   }
   
}
