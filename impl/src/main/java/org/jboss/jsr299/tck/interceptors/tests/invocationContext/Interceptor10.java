package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor10
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      return ctx.getParameters()[0];
   }
}
