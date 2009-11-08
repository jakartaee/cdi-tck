package org.jboss.jsr299.tck.interceptors.tests.method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class DogInterceptor
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      return "Intercepted " + ctx.proceed();
   }
}
