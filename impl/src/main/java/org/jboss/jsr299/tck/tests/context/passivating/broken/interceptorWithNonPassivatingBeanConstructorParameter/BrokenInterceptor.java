package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingBeanConstructorParameter;

import javax.enterprise.inject.Default;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor @InterceptorType
class BrokenInterceptor
{    
   public BrokenInterceptor(@Default Violation violation) {}
   
   @AroundInvoke 
   public Object invoke(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }   
}
