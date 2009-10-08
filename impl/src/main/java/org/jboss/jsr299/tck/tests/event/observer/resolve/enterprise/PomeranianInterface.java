package org.jboss.jsr299.tck.tests.event.observer.resolve.enterprise;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

@Local
public interface PomeranianInterface
{
   public void observeSimpleEvent(@Observes EJBEvent someEvent);
}
