package org.jboss.jsr299.tck.tests.event.observer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

class AnObserver
{
   static boolean wasNotified = false;

   public void observer(@Observes @Any AnEventType event)
   {
      wasNotified = true;
   }
}
