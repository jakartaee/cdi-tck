package org.jboss.jsr299.tck.tests.context.passivating.broken.enterpriseBeanWithNonPassivatingDecorator;

import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful
public class Maarianhamina_Broken implements MaarianHaminaLocal_Broken
{  
   @Remove
   public void bye() {
   }
}
