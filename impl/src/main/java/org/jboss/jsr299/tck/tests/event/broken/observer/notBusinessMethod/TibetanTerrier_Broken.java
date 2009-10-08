package org.jboss.jsr299.tck.tests.event.broken.observer.notBusinessMethod;

import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

@Stateless
class TibetanTerrier_Broken implements Terrier
{
   public void observeSomeEvent(@Observes String someEvent)
   {
   }
}
