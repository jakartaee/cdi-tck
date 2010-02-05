package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Secure
@Transactional
public class OrderInterceptor
{
   @AroundInvoke
   public Object intercept(InvocationContext ctx) throws Exception
   {
      return true;
   }
}
