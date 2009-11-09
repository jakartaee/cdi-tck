package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

class Interceptor1
{
   @PostConstruct
   void postConstruct1(InvocationContext ctx)
   {
      assert LakeCargoShip.getSequence() == 0;
      LakeCargoShip.setSequence(1);
      try
      {
         ctx.proceed();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
}
