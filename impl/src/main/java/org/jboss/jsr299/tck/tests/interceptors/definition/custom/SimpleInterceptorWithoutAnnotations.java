package org.jboss.jsr299.tck.tests.interceptors.definition.custom;

import javax.interceptor.InvocationContext;

class SimpleInterceptorWithoutAnnotations
{
   public Object intercept(InvocationContext ctx) throws Exception {
      return ctx.proceed();
   }
}
