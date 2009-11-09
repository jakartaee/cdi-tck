package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

class PackagePrivateLifecycleInterceptor
{
   private static boolean intercepted = false;

   @PostConstruct
   void postConstruct(InvocationContext ctx)
   {
      intercepted = true;
      try
      {
         ctx.proceed();
      }
      catch (Exception e)
      {
         throw new RuntimeException();
      }
   }

   public static boolean isIntercepted()
   {
      return intercepted;
   }
}
