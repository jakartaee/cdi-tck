package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingInjectedFieldInDecorator;

import java.io.Serializable;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class BrokenDecorator implements EspooLocal_Broken, Serializable
{
   @Inject @Delegate EspooLocal_Broken espooLocal;
   
   @Inject District district;
}
