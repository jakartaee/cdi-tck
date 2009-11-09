package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.interceptor.Interceptors;

@Interceptors(GoatInterceptor.class)
class Goat
{
   private static boolean postConstructInterceptorCalled = false;
   private static boolean preDestroyInterceptorCalled = false;

   @PostConstruct
   public void postConstruct()
   {
      postConstructInterceptorCalled = true;
   }
   
   public String echo(String message)
   {
      return message;
   }
   
   @PreDestroy
   public void preDestroy()
   {
      preDestroyInterceptorCalled = true;
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
