package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;

class OtherSpiderProducer
{
   private static boolean destroyed = false;
   
   @Produces @Tame public Tarantula produceTarantula = new Tarantula();
   
   @PreDestroy
   public void destroy()
   {
      destroyed = true;
   }
   
   public static boolean isDestroyed()
   {
      return destroyed;
   }
   
   public static void setDestroyed(boolean destroyed)
   {
      OtherSpiderProducer.destroyed = destroyed;
   }
}
