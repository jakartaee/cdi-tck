package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.order;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

class Interceptor3
{
   @PostConstruct
   void postConstruct3(InvocationContext ctx)
   {
      assert LakeCargoShip.getSequence() == 2;
      LakeCargoShip.setSequence(3);
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
