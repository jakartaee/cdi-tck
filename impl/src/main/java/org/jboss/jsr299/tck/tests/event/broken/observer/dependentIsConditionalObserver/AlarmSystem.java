package org.jboss.jsr299.tck.tests.event.broken.observer.dependentIsConditionalObserver;

import javax.enterprise.event.Reception;
import javax.enterprise.event.Observes;

class AlarmSystem
{
   public void onBreakInAttempt(@Observes(notifyObserver = Reception.IF_EXISTS) BreakIn breakIn)
   {
      assert false;
   }
}
