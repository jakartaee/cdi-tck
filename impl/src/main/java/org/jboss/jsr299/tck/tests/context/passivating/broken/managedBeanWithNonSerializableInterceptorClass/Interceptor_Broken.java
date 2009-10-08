package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonSerializableInterceptorClass;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class Interceptor_Broken
{
   @AroundInvoke
   public Object intercept(InvocationContext context) throws Exception
   {
      return context.proceed();
   }
}
