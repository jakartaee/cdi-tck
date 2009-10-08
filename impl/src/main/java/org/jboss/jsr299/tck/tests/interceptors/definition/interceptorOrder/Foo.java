package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorOrder;

import javax.interceptor.Interceptors;

@Interceptors({FirstInterceptor.class, SecondInterceptor.class})
class Foo
{
   public void bar() {}
}
