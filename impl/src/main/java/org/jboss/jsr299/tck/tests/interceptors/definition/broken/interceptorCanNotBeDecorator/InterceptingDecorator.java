package org.jboss.jsr299.tck.tests.interceptors.definition.broken.interceptorCanNotBeDecorator;

import javax.decorator.Decorates;
import javax.decorator.Decorator;
import javax.interceptor.Interceptor;

@Decorator
@Interceptor
class InterceptingDecorator
{
   @Decorates Automobile automobile;
}
