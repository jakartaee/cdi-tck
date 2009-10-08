package org.jboss.jsr299.tck.tests.xml.annotationtypes;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@TestInterceptorBindingType @Interceptor
class TestInterceptor
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx)
   {
      return this;
   }
}
