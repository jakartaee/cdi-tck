package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

class Interceptor2
{
   @PostConstruct
   void postConstruct2(InvocationContext ctx)
   {
      assert LakeCargoShip.getSequence() == 1;
      LakeCargoShip.setSequence(2);
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
