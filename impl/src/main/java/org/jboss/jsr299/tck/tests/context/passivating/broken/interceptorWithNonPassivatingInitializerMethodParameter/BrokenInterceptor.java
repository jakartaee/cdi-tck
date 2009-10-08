package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingInitializerMethodParameter;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor @InterceptorType
class BrokenInterceptor
{    
   @Inject
   public void init(Violation violation) {}
   
   @AroundInvoke 
   public Object invoke(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }   
}
