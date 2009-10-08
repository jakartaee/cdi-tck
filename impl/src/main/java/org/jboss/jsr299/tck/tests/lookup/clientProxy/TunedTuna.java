package org.jboss.jsr299.tck.tests.lookup.clientProxy;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class TunedTuna
{
   public String getState()
   {
      return "tuned";
   }
}
