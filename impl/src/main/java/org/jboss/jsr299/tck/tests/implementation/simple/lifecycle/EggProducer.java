package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

public class EggProducer
{
   private static boolean eggDisposed = false;
   
   @Produces @Synchronous
   public Egg nextEgg()
   {
      eggDisposed = false;
      return new Egg();
   }
   
   public void disposeEgg(@Disposes @Synchronous Egg egg)
   {
      eggDisposed = true;
   }

   public static boolean isEggDisposed()
   {
      return eggDisposed;
   }
}
