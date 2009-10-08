package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.useBeforeValidationFails;

import javax.enterprise.event.Observes;

class StringObserver
{
   public static boolean eventReceived = false;

   public void observeStringEvent(@Observes String event)
   {
      eventReceived = true;
   }
}
