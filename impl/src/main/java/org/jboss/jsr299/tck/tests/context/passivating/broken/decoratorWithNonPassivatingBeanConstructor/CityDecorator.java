package org.jboss.jsr299.tck.tests.context.passivating.broken.decoratorWithNonPassivatingBeanConstructor;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@SuppressWarnings("serial")
@Decorator
public class CityDecorator implements CityInterface, Serializable
{
   @Inject @Delegate @Any CityInterface city;
   
   @Inject
   public CityDecorator(NonPassivating nonPassivating) {}
   
   public void foo()
   {
      city.foo();      
   }
}
