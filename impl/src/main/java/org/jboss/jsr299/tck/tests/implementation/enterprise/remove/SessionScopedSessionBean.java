package org.jboss.jsr299.tck.tests.implementation.enterprise.remove;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@Stateful
@SessionScoped
public class SessionScopedSessionBean implements SessionScopedSessionInterface
{
   
   @Remove
   public void remove()
   {
   }

}
