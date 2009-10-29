package org.jboss.jsr299.tck.tests.interceptors.definition.broken.interceptorCanNotBeDecorator;

import javax.decorator.Delegate;
import javax.decorator.Decorator;
import javax.interceptor.Interceptor;

@Decorator
@Interceptor
class InterceptingDecorator
{
   @Delegate Automobile automobile;
}
