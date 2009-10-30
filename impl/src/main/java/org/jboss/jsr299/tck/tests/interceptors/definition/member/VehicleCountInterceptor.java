package org.jboss.jsr299.tck.tests.interceptors.definition.member;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@VehicleCountInterceptorBinding(comment = "bar")
class VehicleCountInterceptor
{
   private static boolean intercepted = false;

   @AroundInvoke
   public Object aroundInvoke(InvocationContext ctx) throws Exception
   {
      intercepted = true;
      return ((Integer) ctx.proceed()) + 10;
   }

   public static boolean isIntercepted()
   {
      return intercepted;
   }
}
