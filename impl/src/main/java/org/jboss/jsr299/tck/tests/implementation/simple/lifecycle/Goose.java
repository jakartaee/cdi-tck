package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.inject.Inject;


public class Goose
{
   @Inject @Synchronous
   private Egg currentEgg;

   public Egg getCurrentEgg()
   {
      return currentEgg;
   }

}
