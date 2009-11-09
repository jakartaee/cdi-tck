package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import javax.annotation.PostConstruct;

class Ship
{
   @PostConstruct
   void postConstruct1()
   {
      assert LakeCargoShip.getSequence() == 4;
      LakeCargoShip.setSequence(5);
   }
}
