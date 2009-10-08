package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import javax.decorator.Decorates;
import javax.decorator.Decorator;

@Decorator
abstract class AnimalDecorator implements Animal
{
   @Decorates Animal delegate;

}
