package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@SuppressWarnings("serial")
public class KokkolaInterceptor implements Serializable
{
   @AroundInvoke
   public Object intercept(InvocationContext context) throws Exception
   {
      // do nothing
      return null;
   }
}
