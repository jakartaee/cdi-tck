package org.jboss.jsr299.tck.tests.extensions.annotated.broken.processInjectionTargetThrowsException;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

class InjectionTargetProcessor implements Extension
{
   public void processDogInjectionTarget(@Observes ProcessInjectionTarget<Dog> injectionTargetEvent)
   {
      throw new RuntimeException();
   }
}
