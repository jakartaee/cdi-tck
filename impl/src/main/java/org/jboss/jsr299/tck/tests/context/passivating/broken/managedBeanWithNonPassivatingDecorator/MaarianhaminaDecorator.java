package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonPassivatingDecorator;

import javax.decorator.Decorates;
import javax.decorator.Decorator;

@Decorator
class MaarianhaminaDecorator 
{   
   @Decorates Maarianhamina_Broken maarianHamina;
}
