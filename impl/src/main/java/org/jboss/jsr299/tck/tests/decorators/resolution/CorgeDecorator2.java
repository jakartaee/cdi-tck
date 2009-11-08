package org.jboss.jsr299.tck.tests.decorators.resolution;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public class CorgeDecorator2
{
   
   @Inject @Delegate
   private Corge<Animal, ? extends FemaleFresianCow> corge;

}
