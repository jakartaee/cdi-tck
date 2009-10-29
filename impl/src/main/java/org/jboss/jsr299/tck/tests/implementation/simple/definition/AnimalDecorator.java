package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
abstract class AnimalDecorator implements Animal
{
   @Inject @Delegate Animal delegate;

}
