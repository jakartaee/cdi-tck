package org.jboss.jsr299.tck.tests.deployment.lifecycle;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@InterceptorType1 @Interceptor
class Interceptor1
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
