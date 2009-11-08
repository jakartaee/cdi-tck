package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor1
{
   private static boolean getTargetOK = false;

   @AroundInvoke
   public Object aroundInvoke(InvocationContext ctx) throws Exception
   {
      SimpleBean target = (SimpleBean) ctx.getTarget();
      int id1 = target.getId();
      int id2 = (Integer) ctx.proceed();

      if (id1 == id2)
      {
         getTargetOK = true;
      }
      return id1;
   }

   public static boolean isGetTargetOK()
   {
      return getTargetOK;
   }
}
