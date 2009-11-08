package org.jboss.jsr299.tck.tests.decorators.resolution;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public class GraultSuperDecorator
{
   
   @Inject @Delegate
   private Grault<? super Integer> grault;

}
