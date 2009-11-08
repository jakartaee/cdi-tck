package org.jboss.jsr299.tck.interceptors.tests.method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class FishInterceptor
{
   private static int instanceCount = 0;
   
   public FishInterceptor()
   {
      instanceCount++;
   }
   
   public static int getInstanceCount()
   {
      return instanceCount;
   }

   @SuppressWarnings("unused")
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      return "Intercepted " + ctx.proceed();
   }
}
