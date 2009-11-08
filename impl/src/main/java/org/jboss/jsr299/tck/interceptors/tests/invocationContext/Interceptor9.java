package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor9
{
   private static boolean contextDataOK = false;
   
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception {
      contextDataOK = ctx.getContextData().get("foo").equals("bar");
      ctx.getContextData().put("foo", "barbar");
      return ctx.proceed();
   }

   public static boolean isContextDataOK()
   {
      return contextDataOK;
   }
}
