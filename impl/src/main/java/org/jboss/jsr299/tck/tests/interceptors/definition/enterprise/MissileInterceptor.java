package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor @Airborne
class MissileInterceptor implements Serializable
{
   public static boolean intercepted = false;
   
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      intercepted = true;
      return ctx.proceed();
   }
}
