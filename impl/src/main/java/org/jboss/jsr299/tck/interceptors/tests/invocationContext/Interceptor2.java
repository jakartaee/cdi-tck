package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor2
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.getTimer() == null;
   }
}
