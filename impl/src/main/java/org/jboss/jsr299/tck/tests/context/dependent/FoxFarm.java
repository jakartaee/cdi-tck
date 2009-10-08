package org.jboss.jsr299.tck.tests.context.dependent;

import javax.inject.Inject;

class FoxFarm
{
   @Inject public Fox fox;      
   
   public Fox constructorFox;
  
   @Inject
   public FoxFarm(Fox fox)
   {
      this.constructorFox = fox;
   }   
}
