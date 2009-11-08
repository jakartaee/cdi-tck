package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.order;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class RailVehicle extends Vehicle
{
   @AroundInvoke
   public Object intercept2(InvocationContext ctx) throws Exception
   {
      int id = (Integer) ctx.proceed();
      assert id == 1;
      return id + 1;
   }
}
