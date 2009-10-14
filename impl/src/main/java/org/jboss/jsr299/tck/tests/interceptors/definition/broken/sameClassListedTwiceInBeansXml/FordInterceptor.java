package org.jboss.jsr299.tck.tests.interceptors.definition.broken.sameClassListedTwiceInBeansXml;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.interceptor.Interceptor;

@Interceptor @Transactional
class FordInterceptor
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
