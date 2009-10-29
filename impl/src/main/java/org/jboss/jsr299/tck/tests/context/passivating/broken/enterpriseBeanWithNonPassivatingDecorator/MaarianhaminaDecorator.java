package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingDecorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class MaarianhaminaDecorator implements MaarianHaminaLocal_Broken
{
   
   @Inject @Delegate MaarianHaminaLocal_Broken maarianHamina;
}
