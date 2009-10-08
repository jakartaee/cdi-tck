package org.jboss.jsr299.tck.tests.extensions.annotated.broken.processAnnotatedObserverThrowsException;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension
{
   public void observeAnnotatedType1(@Observes ProcessAnnotatedType<Dog> event)
   {
      throw new RuntimeException();
   }
}
