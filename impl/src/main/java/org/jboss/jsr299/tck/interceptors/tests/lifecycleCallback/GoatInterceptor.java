package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class GoatInterceptor
{
   private static boolean postConstructInterceptorCalled = false;
   private static boolean preDestroyInterceptorCalled = false;

   @PostConstruct
   public void postConstruct(InvocationContext ctx)
   {
      postConstructInterceptorCalled = true;
      try
      {
         ctx.proceed();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      return ctx.proceed() + ctx.getParameters()[0].toString();
   }
   
   @PreDestroy
   public void preDestroy(InvocationContext ctx)
   {
      preDestroyInterceptorCalled = true;
      try
      {
         ctx.proceed();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   public static boolean isPostConstructInterceptorCalled()
   {
      return postConstructInterceptorCalled;
   }

   public static boolean isPreDestroyInterceptorCalled()
   {
      return preDestroyInterceptorCalled;
   }
}
