package org.jboss.jsr299.tck.interceptors.tests.method;

import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;

@Interceptors(DogInterceptor.class)
class Dog
{
   public String foo()
   {
      return "bar";
   }

   @ExcludeClassInterceptors
   public String ping()
   {
      return "pong";
   }
}
