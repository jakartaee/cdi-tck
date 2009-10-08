package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInjectedFieldInInterceptor;

import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
class BrokenInterceptor
{
   @Inject District district;
   
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
