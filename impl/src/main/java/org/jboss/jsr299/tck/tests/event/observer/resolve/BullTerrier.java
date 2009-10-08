package org.jboss.jsr299.tck.tests.event.observer.resolve;

import javax.enterprise.event.Observes;

class BullTerrier
{
   private static boolean multiBindingEventObserved = false;
   private static boolean singleBindingEventObserved = false;
   
   public void observesMultiBindingEvent(@Observes @Role("Admin") @Tame MultiBindingEvent someEvent)
   {
      multiBindingEventObserved = true;
   }
   
   public void observesSingleBindingEvent(@Observes @Tame MultiBindingEvent someEvent)
   {
      singleBindingEventObserved = true;
   }

   public static boolean isMultiBindingEventObserved()
   {
      return multiBindingEventObserved;
   }

   public static boolean isSingleBindingEventObserved()
   {
      return singleBindingEventObserved;
   }
   
   public static void reset()
   {
      multiBindingEventObserved = false;
      singleBindingEventObserved = false;
   }
}
