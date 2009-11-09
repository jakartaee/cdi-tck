package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import javax.interceptor.Interceptors;

class Sheep
{
   @Interceptors(SheepInterceptor.class)
   public String foo()
   {
      return "bar";
   }
}
