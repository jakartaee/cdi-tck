package org.jboss.jsr299.tck.tests.implementation.initializer.broken.parameterAnnotatedObserves;

import javax.enterprise.event.Observes;
import javax.inject.Inject;


class Grouse_Broken
{
   
   @Inject
   public void setName(String name, @Observes DangerCall dangerCall)
   {
      // No-op
   }
   
}
