package org.jboss.jsr299.tck.tests.interceptors.definition;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Atomic @Interceptor
class NotEnabledAtomicInterceptor
{
   public static boolean intercepted = false;

   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      intercepted = true;
      return ctx.proceed();
   }
}