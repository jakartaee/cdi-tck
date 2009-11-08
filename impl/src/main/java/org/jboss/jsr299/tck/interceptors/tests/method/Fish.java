package org.jboss.jsr299.tck.interceptors.tests.method;

import javax.interceptor.Interceptors;

class Fish
{
   @Interceptors(FishInterceptor.class)
   public String foo() {
      return "bar";
   }
   
   @Interceptors(FishInterceptor.class)
   public String ping() {
      return "pong";
   }
   
   public String getName() {
      return "Salmon";
   }
}
