package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;


public class Goose
{
   @Synchronous
   private Egg currentEgg;

   public Egg getCurrentEgg()
   {
      return currentEgg;
   }

}
