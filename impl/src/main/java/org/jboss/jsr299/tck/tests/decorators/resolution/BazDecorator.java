package org.jboss.jsr299.tck.tests.decorators.resolution;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public class BazDecorator
{
   
   @Inject @Delegate
   private Baz<Object> baz;

}
