package org.jboss.jsr299.tck.tests.context.passivating;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@Stateful
@SessionScoped
class Turku implements TurkuLocal
{
   @Remove
   public void bye() {
      
   }
}
