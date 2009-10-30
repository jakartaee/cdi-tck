package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorOrder;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class AnotherInterceptor
{
   public static boolean first = false;
   
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      if (!TransactionalInterceptor.first) first = true;
      return ctx.proceed();
   }
}
