package org.jboss.jsr299.tck.tests.implementation.initializer;

import javax.inject.Inject;

class ChickenHutch
{
   
   public Fox fox;
   public Chicken chicken;
   
   @Inject
   public void setFox(Fox fox)
   {
      this.fox = fox;
   }
   
   @Inject
   public void setChicken(Chicken chicken)
   {
      this.chicken = chicken;
   }
   
}
