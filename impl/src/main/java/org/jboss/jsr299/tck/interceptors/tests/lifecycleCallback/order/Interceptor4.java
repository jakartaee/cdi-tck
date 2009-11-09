package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

class Interceptor4
{
   @PostConstruct
   void postConstruct(InvocationContext ctx)
   {
      assert LakeCargoShip.getSequence() == 3;
      LakeCargoShip.setSequence(4);
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
