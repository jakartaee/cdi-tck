package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInjectedFieldInInterceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class BrokenInterceptor
{
   @Inject District district;

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
