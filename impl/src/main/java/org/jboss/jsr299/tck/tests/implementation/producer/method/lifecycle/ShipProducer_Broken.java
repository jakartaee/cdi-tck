package org.jboss.jsr299.tck.tests.implementation.producer.method.lifecycle;

import javax.enterprise.inject.Produces;

class ShipProducer_Broken
{
   
   public @Produces @Fail Ship produceShip() throws FooException
   {
      throw new FooException();
   }
   
}
