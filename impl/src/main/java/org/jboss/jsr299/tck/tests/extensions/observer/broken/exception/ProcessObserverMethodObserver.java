package org.jboss.jsr299.tck.tests.extensions.observer.broken.exception;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessObserverMethod;

class ProcessObserverMethodObserver implements Extension
{
   public void observeObserverMethodForEventC(@Observes ProcessObserverMethod<?, EventC> event)
   {
      throw new RuntimeException("Definition error for EventC");
   }
}
