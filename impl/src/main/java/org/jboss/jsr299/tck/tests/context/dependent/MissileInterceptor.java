package org.jboss.jsr299.tck.tests.context.dependent;

import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
class MissileInterceptor
{
   public Object intercept(InvocationContext context) throws Exception
   {
      return context.proceed();
   }
}
