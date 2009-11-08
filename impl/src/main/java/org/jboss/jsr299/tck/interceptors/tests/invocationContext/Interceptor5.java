package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor5
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      Integer[] parameters = new Integer[] { 1, 2, 3 };
      ctx.setParameters(parameters);
      return ctx.proceed();
   }
}
