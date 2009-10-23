package org.jboss.jsr299.tck.tests.lookup.clientProxy;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;



@ApplicationScoped
class Garage
{
   @Inject
   private Car car;

   public String getMakeOfTheParkedCar()
   {
      return car.getMake();
   }
}
