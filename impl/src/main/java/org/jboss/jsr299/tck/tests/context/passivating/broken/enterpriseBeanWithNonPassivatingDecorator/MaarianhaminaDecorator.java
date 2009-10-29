package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingDecorator;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
class MaarianhaminaDecorator implements MaarianHaminaLocal_Broken
{
   
   @Delegate MaarianHaminaLocal_Broken maarianHamina;
}
