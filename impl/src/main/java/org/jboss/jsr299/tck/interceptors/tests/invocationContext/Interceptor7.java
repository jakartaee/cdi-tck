package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor7
{
   private static boolean proceedReturnsNull = false;
   
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      proceedReturnsNull = ctx.proceed() == null;
      return null;
   }

   public static boolean isProceedReturnsNull()
   {
      return proceedReturnsNull;
   }
}
