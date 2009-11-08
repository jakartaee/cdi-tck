package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

class PackagePrivateInterceptor
{
   @AroundInvoke
   Object intercept(InvocationContext ctx) throws Exception {
      return ((Integer) ctx.proceed()) + 1;
   }
}
