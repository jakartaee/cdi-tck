package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class AnimalDecorator implements Animal
{
   @Inject @Delegate Animal bean;

   public String hello()
   {
      return bean.hello() + " world!";
   }
}
