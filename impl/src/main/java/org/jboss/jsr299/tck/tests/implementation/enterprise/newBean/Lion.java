package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Remove;
import javax.ejb.Stateful;

@Tame
@Stateful
class Lion implements LionLocal
{
   
   @Remove
   public void remove()
   {
      
   }
   
}
