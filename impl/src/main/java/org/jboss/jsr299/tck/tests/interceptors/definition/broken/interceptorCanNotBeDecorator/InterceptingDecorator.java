package org.jboss.jsr299.tck.tests.interceptors.definition.broken.interceptorCanNotBeDecorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

@Decorator
@Interceptor
class InterceptingDecorator
{
   @Inject @Delegate Automobile automobile;
}
