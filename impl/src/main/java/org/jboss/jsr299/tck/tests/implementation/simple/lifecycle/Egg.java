package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.annotation.PreDestroy;

public class Egg
{
   private static boolean eggDestroyed = false;
   
   public Egg()
   {
      eggDestroyed = false;
   }

   @PreDestroy
   public void destroy()
   {
      eggDestroyed = true;
   }

   public static boolean isEggDestroyed()
   {
      return eggDestroyed;
   }
}
