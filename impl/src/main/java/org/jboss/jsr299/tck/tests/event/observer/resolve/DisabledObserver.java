package org.jboss.jsr299.tck.tests.event.observer.resolve;

import javax.enterprise.event.Observes;

@NotEnabled class DisabledObserver
{
   public void observeSecret(@Observes @Secret String secretString)
   {
      if ("fail if disabled observer invoked".equals(secretString))
      {
         assert false : "This observer should not be invoked since it resides on a bean with a policy that is not enabled.";
      }
   }
   
   public void observeGhost(@Observes Ghost ghost)
   {
      assert false : "This observer should not be invoked since it resides on a bean with a policy that is not enabled.";
   }
}
