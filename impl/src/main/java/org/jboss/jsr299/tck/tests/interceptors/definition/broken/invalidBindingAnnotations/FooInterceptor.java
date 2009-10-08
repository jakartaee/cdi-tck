package org.jboss.jsr299.tck.tests.interceptors.definition.broken.invalidBindingAnnotations;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@FooBinding("abc") @Interceptor
class FooInterceptor
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
