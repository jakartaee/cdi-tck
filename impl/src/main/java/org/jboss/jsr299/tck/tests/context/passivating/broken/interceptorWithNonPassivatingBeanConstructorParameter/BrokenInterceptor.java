package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingBeanConstructorParameter;

import java.io.Serializable;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@BakedBinding
class BrokenInterceptor implements Serializable
{
   @Inject
   public BrokenInterceptor(@Default Violation violation)
   {
   }

   @AroundInvoke
   public Object invoke(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
