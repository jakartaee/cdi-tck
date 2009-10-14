package org.jboss.jsr299.tck.tests.interceptors.definition;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Logged @Interceptor
class NetworkLogger
{
   public static boolean intercepted = false;

   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      intercepted = true;
      return ctx.proceed();
   }
}
