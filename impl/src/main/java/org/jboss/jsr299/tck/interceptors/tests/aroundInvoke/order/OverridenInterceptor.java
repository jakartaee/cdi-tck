package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.order;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class OverridenInterceptor
{
   private static boolean overridenMethodCalled = false;
   
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      overridenMethodCalled = true;
      return ctx.proceed();
   }

   public static boolean isOverridenMethodCalled()
   {
      return overridenMethodCalled;
   }
   
   
}
