package org.jboss.jsr299.tck.tests.extensions.annotated;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@GroceryInterceptorBinding
class GroceryInterceptor
{
   @AroundInvoke
   public Object interceptFoo(InvocationContext ctx) throws Exception
   {
      if (ctx.getMethod().getName().equals("foo"))
      {
         return "foo";
      }
      else
      {
         return ctx.proceed();
      }
   }
}
