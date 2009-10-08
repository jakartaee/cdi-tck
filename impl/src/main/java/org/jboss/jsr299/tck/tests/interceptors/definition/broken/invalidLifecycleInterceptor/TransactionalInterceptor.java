package org.jboss.jsr299.tck.tests.interceptors.definition.broken.invalidLifecycleInterceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Transactional @Interceptor 
class TransactionalInterceptor
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
