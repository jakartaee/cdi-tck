package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor3
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.getMethod().equals(SimpleBean.class.getMethod("testGetMethod"));
   }
}
