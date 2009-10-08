package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorCalledBeforeDecorator;

import javax.interceptor.Interceptors;

@Interceptors(TransactionInterceptor.class)
class Foo
{
   public static boolean interceptorCalledFirst;
   public static boolean decoratorCalledFirst;
   
   public void bar() {}
}
