package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
class CityDecorator implements CityInterface, Serializable
{
   @Inject @Delegate @Any CityInterface city; 
   
   public void foo()
   {
      city.foo();      
   }
}
