package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingBeanConstructorParameterInInterceptor;

import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
class BrokenInterceptor
{
   public BrokenInterceptor(District district) {}
   
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
