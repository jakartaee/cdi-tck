package org.jboss.jsr299.tck.tests.event.observer.resolve.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.event.Observes;

public
@Stateful
class Spitz implements PomeranianInterface
{
   public void observeSimpleEvent(@Observes EJBEvent someEvent)
   {
   }

   public static void staticallyObserveEvent(@Observes EJBEvent someEvent)
   {
   }
}
