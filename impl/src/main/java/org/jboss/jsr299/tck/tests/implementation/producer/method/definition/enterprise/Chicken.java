package org.jboss.jsr299.tck.tests.implementation.producer.method.definition.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;

@Stateful
public class Chicken implements ChickenLocal
{
   
   @Produces @Yummy
   public Egg produceEgg()
   {
      return new Egg(this);
   }

}