package org.jboss.jsr299.tck.tests.implementation.initializer;

import javax.inject.Inject;

class StandardChickenHutch
{
   private ChickenInterface chicken;

   @Inject
   public void setChicken(@StandardVariety ChickenInterface chicken)
   {
      this.chicken = chicken;
   }

   public ChickenInterface getChicken()
   {
      return chicken;
   }
   
}
