package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInitializerInDecorator;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class BrokenDecorator implements EspooLocal_Broken, Serializable
{
   @Inject @Delegate EspooLocal_Broken espooLocal;
   
   @Inject
   public void init(District district) {}
}
