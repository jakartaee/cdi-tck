package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.decorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class CatDecorator extends Cat
{
   @Inject @Delegate Cat bean;

   @Override
   public String hello()
   {
      return bean.hello() + " world!";
   }
}
