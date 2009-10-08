package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class ContextExtensions implements Extension
{
   public void addNewContexts(@Observes AfterBeanDiscovery event)
   {
      event.addContext(new DummyContext());
      event.addContext(new DummyContext());
   }
}
