package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.inject.Produces;

class CityProducer2
{
   @Produces @Big
   public Violation create()
   {
      return new Violation();
   }
}
