package org.jboss.jsr299.tck.interceptors.tests.aroundInvoke;

import javax.interceptor.Interceptors;

import org.jboss.jsr299.tck.interceptors.tests.aroundInvoke.PackagePrivateInterceptor;


class SimpleBean
{
   @Interceptors(PrivateInterceptor.class)
   public int zero() {
      return 0;
   }
   
   @Interceptors(ProtectedInterceptor.class)
   public int one() {
      return 1;
   }
   
   @Interceptors(PackagePrivateInterceptor.class)
   public int two() {
      return 2;
   }
}
