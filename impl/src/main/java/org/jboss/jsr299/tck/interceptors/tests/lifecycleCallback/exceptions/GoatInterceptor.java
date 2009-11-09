package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback.exceptions;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

class GoatInterceptor
{
   private static boolean exceptionCaught = false;

   @PostConstruct
   public void postConstruct(InvocationContext ctx)
   {
      try
      {
         ctx.proceed();
      }
      catch (IllegalStateException ise)
      {
         exceptionCaught = true;
      }
      catch (Exception e)
      {
      }
   }

   public static boolean isExceptionCaught()
   {
      return exceptionCaught;
   }
}
