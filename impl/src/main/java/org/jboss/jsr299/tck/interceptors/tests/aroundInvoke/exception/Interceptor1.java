package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.exception;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor1
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      try
      {
         return ctx.proceed();
      } catch (NoSuchMethodException e) {
         return true;
      }
   }
}
