package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.interceptor;

class Cat
{
   @CatInterceptorBinding
   public String hello() {
      return "hello";
   }
}
