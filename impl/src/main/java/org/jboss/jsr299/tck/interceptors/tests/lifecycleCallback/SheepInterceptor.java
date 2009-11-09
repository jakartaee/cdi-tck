package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import javax.annotation.PostConstruct;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class SheepInterceptor
{
   private static boolean postConstructCalled = false;
   private static boolean aroundInvokeCalled = false;
   
   @PostConstruct
   public void postConstruct()
   {
      postConstructCalled = true;
   }
   
   @AroundInvoke
   public Object aroundInvoke(InvocationContext ctx) throws Exception
   {
      aroundInvokeCalled = true;
      return ctx.proceed();
   }

   public static boolean isPostConstructCalled()
   {
      return postConstructCalled;
   }

   public static boolean isAroundInvokeCalled()
   {
      return aroundInvokeCalled;
   }
}