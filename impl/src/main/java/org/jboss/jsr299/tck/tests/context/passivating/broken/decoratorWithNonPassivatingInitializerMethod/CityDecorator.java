package org.jboss.jsr299.tck.tests.context.passivating.broken.decoratorWithNonPassivatingInitializerMethod;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@SuppressWarnings("serial")
@Decorator
class CityDecorator implements CityInterface, Serializable
{
   @Inject @Delegate @Any CityInterface city;
   
   @Inject
   public void init(NonPassivating nonPassivating) {}
   
   public void foo()
   {
      city.foo();      
   }
}
