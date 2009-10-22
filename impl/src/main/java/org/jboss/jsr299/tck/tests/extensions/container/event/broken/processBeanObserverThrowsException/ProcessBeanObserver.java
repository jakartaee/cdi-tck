package org.jboss.jsr299.tck.tests.extensions.container.event.broken.processBeanObserverThrowsException;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;

public class ProcessBeanObserver implements Extension
{
   public void observe(@Observes ProcessBean<Sheep> event) {
      throw new RuntimeException();
   }
}
