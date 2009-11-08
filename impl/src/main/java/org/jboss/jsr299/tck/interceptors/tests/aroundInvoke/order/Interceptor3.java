package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.order;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor3 extends Interceptor2
{
   @AroundInvoke
   public Object intercept2(InvocationContext ctx) throws Exception
   {
      int id = (Integer) ctx.proceed();
      assert id == 5;
      return id + 1;
   }
}
