package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@CatInterceptorBinding
class CatInterceptor extends Cat
{

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      return ctx.proceed();
   }
}
