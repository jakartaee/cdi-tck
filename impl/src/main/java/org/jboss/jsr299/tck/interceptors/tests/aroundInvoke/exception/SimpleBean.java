package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.exception;

import javax.interceptor.Interceptors;

@Interceptors( { Interceptor1.class, Interceptor2.class })
class SimpleBean
{
   public boolean foo()
   {
      throw new RuntimeException();
   }
}
