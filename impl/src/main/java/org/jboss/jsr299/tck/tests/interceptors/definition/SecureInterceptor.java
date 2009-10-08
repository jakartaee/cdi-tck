package org.jboss.jsr299.tck.tests.interceptors.definition;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Secure @Interceptor
class SecureInterceptor
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }

}
