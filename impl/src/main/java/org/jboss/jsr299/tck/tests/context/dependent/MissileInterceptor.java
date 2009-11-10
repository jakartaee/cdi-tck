package org.jboss.jsr299.tck.tests.context.dependent;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class MissileInterceptor
{
   @AroundInvoke
   public Object intercept(InvocationContext context) throws Exception
   {
      return context.proceed();
   }
}
