package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import java.util.HashSet;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor6
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      Object[] parameters = new Object[]{"1", new HashSet<Integer>()};
      ctx.setParameters(parameters);
      return ctx.proceed();
   }
}
