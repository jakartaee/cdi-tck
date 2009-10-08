package org.jboss.jsr299.tck.tests.event.observer.resolve;

import javax.enterprise.event.Observes;
import javax.inject.Named;

@Named("Teddy")
class Pomeranian
{
   public static Thread notificationThread;
   
   public void observeSimpleEvent(@Observes SimpleEventType someEvent)
   {
      notificationThread = Thread.currentThread();
   }
   
   public void observerTameSimpleEvent(@Observes @Tame SimpleEventType someEvent)
   {
   }

   public static void staticallyObserveEvent(@Observes SimpleEventType someEvent)
   {
   }
}
