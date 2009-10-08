package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorOrder;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Transactional @Interceptor
class TransactionalInterceptor
{
   public static boolean first = false;
   
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      if (!AnotherInterceptor.first) first = true;      
      return ctx.proceed();
   }

}
