package org.jboss.jsr299.tck.tests.event.broken.observer.dependentIsConditionalObserver;

import javax.enterprise.event.Notify;
import javax.enterprise.event.Observes;

class AlarmSystem
{
   public void onBreakInAttempt(@Observes(notifyObserver = Notify.IF_EXISTS) BreakIn breakIn)
   {
      assert false;
   }
}
