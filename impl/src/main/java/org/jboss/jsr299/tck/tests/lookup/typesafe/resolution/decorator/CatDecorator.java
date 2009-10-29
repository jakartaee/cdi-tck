package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution.decorator;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
class CatDecorator extends Cat
{
   @Delegate Cat bean;

   public String hello()
   {
      return bean.hello() + " world!";
   }
}
