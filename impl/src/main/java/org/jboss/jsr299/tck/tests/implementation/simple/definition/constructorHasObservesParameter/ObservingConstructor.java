package org.jboss.jsr299.tck.tests.implementation.simple.definition.constructorHasObservesParameter;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

class ObservingConstructor
{

   @Inject
   public ObservingConstructor(@Observes Duck duck)
   {
      
   }
}
