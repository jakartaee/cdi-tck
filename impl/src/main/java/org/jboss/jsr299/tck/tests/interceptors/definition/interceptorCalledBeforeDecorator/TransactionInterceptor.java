package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorCalledBeforeDecorator;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

public class TransactionInterceptor
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      if (!Foo.decoratorCalledFirst) Foo.interceptorCalledFirst = true;
      
      return ctx.proceed();
   }
}
