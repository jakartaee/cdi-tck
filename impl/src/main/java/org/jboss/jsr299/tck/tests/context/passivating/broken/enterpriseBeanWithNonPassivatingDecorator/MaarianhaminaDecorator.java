package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingDecorator;

import javax.decorator.Decorates;
import javax.decorator.Decorator;

@Decorator
class MaarianhaminaDecorator implements MaarianHaminaLocal_Broken
{
   
   @Decorates MaarianHaminaLocal_Broken maarianHamina;
}
