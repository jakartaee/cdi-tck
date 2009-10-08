package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class FarmInterceptor
{

   @Inject
   private Sheep sheep;

   @AroundInvoke
   public Object intercept(InvocationContext invocation) throws Exception
   {
      if (sheep == null)
      {
         throw new RuntimeException("Sheep not injected.");
      }
      return (Integer)invocation.proceed() + 1;  
   }
}
