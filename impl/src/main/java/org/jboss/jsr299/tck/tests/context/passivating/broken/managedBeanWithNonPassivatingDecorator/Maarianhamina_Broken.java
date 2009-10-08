package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonPassivatingDecorator;

import javax.ejb.Remove;
import javax.enterprise.context.SessionScoped;

@SessionScoped
class Maarianhamina_Broken
{  
   @Remove
   public void bye() {
   }
}
