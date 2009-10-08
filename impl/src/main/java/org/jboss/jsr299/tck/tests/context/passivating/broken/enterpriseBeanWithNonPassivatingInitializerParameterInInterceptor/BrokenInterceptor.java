package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInitializerParameterInInterceptor;

import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
class BrokenInterceptor
{
   @Inject
   public void init(District district) {}
   
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
