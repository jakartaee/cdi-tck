package org.jboss.jsr299.tck.interceptors.tests.definition;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
class ClassWithAroundInvokeMethod
{
   public String foo()
   {
      return "bar";
   }

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.proceed() + "bar";
   }
}
