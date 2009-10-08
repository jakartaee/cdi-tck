package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import javax.enterprise.inject.Produces;

class Chicken
{
   
   @Produces @Yummy
   public Egg produceEgg()
   {
      return new Egg(this);
   }

}
