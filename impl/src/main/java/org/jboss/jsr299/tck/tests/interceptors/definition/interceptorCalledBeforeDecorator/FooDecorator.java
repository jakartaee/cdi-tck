package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorCalledBeforeDecorator;

import javax.decorator.Decorates;
import javax.decorator.Decorator;

@Decorator
class FooDecorator
{
   @Decorates Foo delegate;
   
   public void bar()
   {
      if (!Foo.interceptorCalledFirst) Foo.decoratorCalledFirst = true;
      
      delegate.bar();
   }
}
