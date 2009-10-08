package org.jboss.jsr299.tck.tests.interceptors.definition.multipleBindings;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor @Fast @Deadly
class MissileInterceptor
{
   public static boolean intercepted = false;
   
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      intercepted = true;
      return ctx.proceed();
   }
}
