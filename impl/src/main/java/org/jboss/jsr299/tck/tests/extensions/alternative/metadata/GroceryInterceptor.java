package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@SuppressWarnings("serial")
@Interceptor
@GroceryInterceptorBinding
class GroceryInterceptor implements Serializable
{
   @AroundInvoke
   public Object interceptFoo(InvocationContext ctx) throws Exception
   {
      if (ctx.getMethod().getName().equals("foo"))
      {
         return "foo";
      }
      return ctx.proceed();
   }
}
