package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
abstract class AnimalDecorator implements Animal
{
   @Delegate Animal delegate;

}
