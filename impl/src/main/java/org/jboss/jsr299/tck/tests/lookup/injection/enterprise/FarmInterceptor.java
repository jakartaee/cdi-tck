package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class FarmInterceptor
{

   @Inject
   private Sheep sheep;
   private boolean initializerCalled = false;

   @Inject
   public void initialize(Sheep sheep)
   {
      initializerCalled = sheep != null;
   }

   @AroundInvoke
   public Object intercept(InvocationContext invocation) throws Exception
   {
      if ((sheep != null) && initializerCalled)
      {
         return (Integer) invocation.proceed() + 1;
      }
      else
      {
         throw new RuntimeException("Sheep not injected.");
      }
   }
}
