package org.jboss.jsr299.tck.interceptors.tests.invocationContext;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor4
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      Object[] parameters = ctx.getParameters();
      assert (Integer) parameters[0] == 1;
      assert (Integer) parameters[1] == 2;
      // modify parameter values
      Integer[] newParameters = new Integer[] { 2, 3 };
      ctx.setParameters(newParameters);
      // verify getParameters() returns actual values
      assert newParameters[0].equals(ctx.getParameters()[0]);
      assert newParameters[1].equals(ctx.getParameters()[1]);
      return ctx.proceed();
   }
}
