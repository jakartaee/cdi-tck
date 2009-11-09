package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;

@Interceptors( { Interceptor3.class, Interceptor4.class })
class CargoShip extends Ship
{
   @PostConstruct
   void postConstruct2()
   {
      assert LakeCargoShip.getSequence() == 5;
      LakeCargoShip.setSequence(6);
   }
}
