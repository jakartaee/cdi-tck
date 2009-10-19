package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingBeanConstructorParameterInInterceptor;

import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.interceptor.AroundInvoke;
import javax.inject.Inject;
import java.io.Serializable;

@Interceptor @CityBinding
class BrokenInterceptor implements Serializable
{
   @Inject
   public BrokenInterceptor(District district) {}

   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
