package org.jboss.jsr299.tck.tests.event.bindingTypes;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

class EventEmitter
{
   @Inject @Any Event<String> stringEvent;
   
   @Inject @Any @NonRuntimeBindingType Event<String> stringEventWithAnyAndNonRuntimeBindingType;
   
   @Inject @NonRuntimeBindingType Event<String> stringEventWithOnlyNonRuntimeBindingType;
   
   public void fireEvent()
   {
      stringEvent.fire("event");
   }
   
   public void fireEventWithNonRuntimeBindingType()
   {
      stringEventWithAnyAndNonRuntimeBindingType.fire("event");
   }
}
