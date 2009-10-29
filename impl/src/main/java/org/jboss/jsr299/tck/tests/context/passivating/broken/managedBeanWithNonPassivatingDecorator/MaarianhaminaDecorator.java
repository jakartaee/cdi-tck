package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonPassivatingDecorator;

import javax.decorator.Delegate;
import javax.decorator.Decorator;

@Decorator
class MaarianhaminaDecorator 
{   
   @Delegate Maarianhamina_Broken maarianHamina;
}
