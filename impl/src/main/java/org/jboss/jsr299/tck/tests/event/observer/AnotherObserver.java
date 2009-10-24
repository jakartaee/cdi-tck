package org.jboss.jsr299.tck.tests.event.observer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Reception;
import javax.enterprise.event.Observes;

@RequestScoped
class AnotherObserver
{
   static boolean wasNotified = false;

   void observer(@Observes @Role("Admin") AnEventType event)
   {
      wasNotified = true;
   }
   
   void conditionalObserve(@Observes(notifyObserver = Reception.IF_EXISTS) ConditionalEvent e)
   {
   }
}
