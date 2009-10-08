package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.failsDuringBeanDiscovery;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

class InitializerBean_Broken
{
   @Inject
   public void initialize(@Observes String string)
   {
   }
}
