package org.jboss.jsr299.tck.tests.event.broken.observer.beanNotManaged;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

abstract class AbstractBean
{
   public abstract void observer(@Observes @Any String event);
}
