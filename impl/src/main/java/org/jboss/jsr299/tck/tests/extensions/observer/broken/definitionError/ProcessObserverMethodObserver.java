package org.jboss.jsr299.tck.tests.extensions.observer.broken.definitionError;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessObserverMethod;

class ProcessObserverMethodObserver implements Extension
{

   public void observeObserverMethodForEventB(@Observes ProcessObserverMethod<?, EventB> event)
   {
      event.addDefinitionError(new RuntimeException("Definition error for EventB"));
   }
}
