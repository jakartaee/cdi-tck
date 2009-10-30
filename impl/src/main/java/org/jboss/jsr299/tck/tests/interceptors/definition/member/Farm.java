package org.jboss.jsr299.tck.tests.interceptors.definition.member;

import org.jboss.jsr299.tck.tests.interceptors.definition.member.AnimalCountInterceptorBinding.Operation;

class Farm
{
   @AnimalCountInterceptorBinding(Operation.INCREASE)
   public int getAnimalCount() {
      return 10;
   }
   
   @VehicleCountInterceptorBinding(comment = "foo")
   public int getVehicleCount() {
      return 10;
   }
}
