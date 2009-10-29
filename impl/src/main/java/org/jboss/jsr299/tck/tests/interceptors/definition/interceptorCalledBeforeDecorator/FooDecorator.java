package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorCalledBeforeDecorator;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
class FooDecorator
{
   @Delegate Foo delegate;
   
   public void bar()
   {
      if (!Foo.interceptorCalledFirst) Foo.decoratorCalledFirst = true;
      
      delegate.bar();
   }
}
