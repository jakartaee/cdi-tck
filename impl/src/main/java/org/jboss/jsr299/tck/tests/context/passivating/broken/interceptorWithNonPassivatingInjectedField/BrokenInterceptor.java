package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingInjectedField;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor @InterceptorType
class BrokenInterceptor
{       
   @Inject Violation violation;
   
   @AroundInvoke 
   public Object invoke(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }   
}
