package org.jboss.jsr299.tck.tests.context.dependent;

import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.interceptor.AroundInvoke;

class MissileInterceptor
{
   @AroundInvoke
   public Object intercept(InvocationContext context) throws Exception
   {
      return context.proceed();
   }
}
