package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Remove;
import javax.ejb.Stateful;

@Stateful
class Order implements OrderLocal
{
   
   @Remove
   public void remove()
   {
      
   }

}
