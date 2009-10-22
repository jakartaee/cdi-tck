package org.jboss.jsr299.tck.tests.extensions.container.event;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class SheepInterceptor
{
   @AroundInvoke
   public Object intercept(InvocationContext invocation) throws Exception
   {
      return invocation.proceed();  
   }
}
