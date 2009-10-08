package org.jboss.jsr299.tck.tests.xml.annotationtypes;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@InterceptorType2
class Interceptor2
{
   @AroundInvoke public Object alwaysReturnThis(InvocationContext ctx)
   {
      InterceptorRecorder interceptorRecorder = null;
      try
      {
         interceptorRecorder = (InterceptorRecorder) ctx.proceed();
         interceptorRecorder.addInterceptor(this);
      }
      catch (Exception e)
      {
      }
      return interceptorRecorder;
   }
}
