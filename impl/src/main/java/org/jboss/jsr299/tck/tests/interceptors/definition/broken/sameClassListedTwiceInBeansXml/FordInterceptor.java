package org.jboss.jsr299.tck.tests.interceptors.definition.broken.sameClassListedTwiceInBeansXml;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor @Transactional
class FordInterceptor
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx) throws Exception
   {
      return ctx.proceed();
   }
}
