package org.jboss.jsr299.tck.tests.decorators.resolution;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public class FooDecorator<T>
{
   
   @Inject @Delegate
   private Foo<T> foo;

}
