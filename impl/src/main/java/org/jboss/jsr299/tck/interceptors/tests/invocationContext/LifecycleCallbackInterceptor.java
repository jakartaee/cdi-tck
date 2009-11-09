package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

class LifecycleCallbackInterceptor
{
   private static boolean getMethodReturnsNull = false;

   @PostConstruct
   public void postConstruct(InvocationContext ctx)
   {
      getMethodReturnsNull = ctx.getMethod() == null;
      try
      {
         ctx.proceed();
      }
      catch (Exception e)
      {
      }
   }

   public static boolean isGetMethodReturnsNull()
   {
      return getMethodReturnsNull;
   }
}
