package org.jboss.jsr299.tck.tests.extensions.producer.broken.injectionTargetError;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

class EventProcessor implements Extension
{
   public void processDogInjectionTarget(@Observes ProcessInjectionTarget<Dog> injectionTargetEvent)
   {
      // Add an exception as a definition error
      injectionTargetEvent.addDefinitionError(new RuntimeException("Should abort processing after bean discovery"));
   }

}
