package org.jboss.jsr299.tck.tests.event.bindingTypes;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class DiscerningObserver
{
   private int numTimesAnyBindingTypeEventObserved = 0;
   private int numTimesNonRuntimeBindingTypeObserved = 0;

   public void observeAny(@Observes @Any String event)
   {
      numTimesAnyBindingTypeEventObserved++;
   }
   
   public void observeNonRuntime(@Observes @Any @NonRuntimeBindingType String event)
   {
      numTimesNonRuntimeBindingTypeObserved++;
   }
   
   public int getNumTimesAnyBindingTypeEventObserved()
   {
      return numTimesAnyBindingTypeEventObserved;
   }
   
   public int getNumTimesNonRuntimeBindingTypeObserved()
   {
      return numTimesNonRuntimeBindingTypeObserved;
   }
   
   public void reset()
   {
      numTimesAnyBindingTypeEventObserved = 0;
      numTimesNonRuntimeBindingTypeObserved = 0;
   }
}
