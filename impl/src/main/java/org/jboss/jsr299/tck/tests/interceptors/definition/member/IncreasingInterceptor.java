package org.jboss.jsr299.tck.tests.interceptors.definition.member;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.jboss.jsr299.tck.tests.interceptors.definition.member.AnimalCountInterceptorBinding.Operation;

@Interceptor
@AnimalCountInterceptorBinding(Operation.INCREASE)
class IncreasingInterceptor
{
   private static boolean intercepted = false;

   @AroundInvoke
   public Object aroundInvoke(InvocationContext ctx) throws Exception
   {
      intercepted = true;
      return ((Integer) ctx.proceed()) + 10;
   }

   public static boolean isIntercepted()
   {
      return intercepted;
   }
}
