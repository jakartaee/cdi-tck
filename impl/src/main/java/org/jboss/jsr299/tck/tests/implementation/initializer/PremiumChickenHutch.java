package org.jboss.jsr299.tck.tests.implementation.initializer;

import javax.inject.Inject;

class PremiumChickenHutch
{
   private ChickenInterface chicken;

   @Inject
   public void setChicken(@Preferred ChickenInterface chicken)
   {
      this.chicken = chicken;
   }

   public ChickenInterface getChicken()
   {
      return chicken;
   }
   
}
