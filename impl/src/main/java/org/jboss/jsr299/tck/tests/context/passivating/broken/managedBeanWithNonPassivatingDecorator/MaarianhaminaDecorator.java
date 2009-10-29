package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonPassivatingDecorator;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class MaarianhaminaDecorator 
{   
   @Inject @Delegate Maarianhamina_Broken maarianHamina;
}
