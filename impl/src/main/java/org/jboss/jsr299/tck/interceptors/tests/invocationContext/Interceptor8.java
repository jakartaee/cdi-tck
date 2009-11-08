package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor8
{
   private static boolean contextDataOK = false;

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      ctx.getContextData().put("foo", "bar");
      Object result = ctx.proceed();
      contextDataOK = ctx.getContextData().get("foo").equals("barbar");
      return result;
   }

   public static boolean isContextDataOK()
   {
      return contextDataOK;
   }
}