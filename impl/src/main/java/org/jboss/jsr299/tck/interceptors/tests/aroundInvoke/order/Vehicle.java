package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.order;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;


class Vehicle
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      int id = (Integer) ctx.proceed();
      assert id == 2;
      return id + 1;
   }
}
