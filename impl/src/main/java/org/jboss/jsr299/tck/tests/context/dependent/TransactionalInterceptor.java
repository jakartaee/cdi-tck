package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Dependent @Transactional @Interceptor 
class TransactionalInterceptor
{
   public static boolean destroyed = false;
   public static boolean intercepted = false;
   
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      intercepted = true;
      return ctx.proceed();
   }

   @PreDestroy public void destroy()
   {
      destroyed = true;
   }
}
