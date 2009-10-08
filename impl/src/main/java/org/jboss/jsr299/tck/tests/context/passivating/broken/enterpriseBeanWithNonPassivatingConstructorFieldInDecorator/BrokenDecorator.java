package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingConstructorFieldInDecorator;

import java.io.Serializable;

import javax.decorator.Decorates;
import javax.decorator.Decorator;
import javax.inject.Inject;

@Decorator
class BrokenDecorator implements EspooLocal_Broken, Serializable
{
   @Decorates EspooLocal_Broken espooLocal;
   
   @Inject
   public BrokenDecorator(District district) {}
}
