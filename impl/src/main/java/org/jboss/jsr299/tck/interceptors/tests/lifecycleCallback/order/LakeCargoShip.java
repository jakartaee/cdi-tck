package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import javax.annotation.PostConstruct;

class LakeCargoShip extends CargoShip
{
   // Every interceptor sets this property to a certain value.
   // The following interceptor verifies the correct order of the chain by
   // inspecting this value.
   private static int sequence = 0;

   static int getSequence()
   {
      return sequence;
   }

   static void setSequence(int value)
   {
      sequence = value;
   }
   
   @PostConstruct
   void postConstruct3()
   {      
      assert LakeCargoShip.getSequence() == 6;
      LakeCargoShip.setSequence(7);
   }
}
