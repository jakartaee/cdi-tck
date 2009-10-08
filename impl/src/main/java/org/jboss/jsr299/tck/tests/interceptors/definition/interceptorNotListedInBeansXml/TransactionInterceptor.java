package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorNotListedInBeansXml;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Transactional @Interceptor
class TransactionInterceptor
{
   public static boolean invoked = false;
   
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      invoked = true;
      return ctx.proceed();
   }
}
