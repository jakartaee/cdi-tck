package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorOrder;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
public class FirstInterceptor
{
   public static boolean calledFirst = false;
   
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      if (!SecondInterceptor.calledFirst) calledFirst = true;      
      return ctx.proceed();
   }
}
