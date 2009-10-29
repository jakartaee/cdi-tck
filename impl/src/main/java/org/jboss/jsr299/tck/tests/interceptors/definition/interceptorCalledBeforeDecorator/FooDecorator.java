package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorCalledBeforeDecorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class FooDecorator
{
   @Inject @Delegate Foo delegate;
   
   public void bar()
   {
      if (!Foo.interceptorCalledFirst) Foo.decoratorCalledFirst = true;
      
      delegate.bar();
   }
}
