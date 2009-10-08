package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Named;

@Dependent @Named @Default
class Fox
{
   
   private static boolean destroyed = false;
   
   private static int destroyCount = 0;
   
   public static boolean isDestroyed()
   {
      return destroyed;
   }
   
   public static void reset()
   {
      destroyed = false;
      destroyCount = 0;
   }
   
   public static int getDestroyCount()
   {
      return destroyCount;
   }
   
   @PreDestroy
   public void destroy()
   {
      destroyed = true;
      destroyCount++;
   }
   
   public String getName()
   {
      return "gavin";
   }
   
}
