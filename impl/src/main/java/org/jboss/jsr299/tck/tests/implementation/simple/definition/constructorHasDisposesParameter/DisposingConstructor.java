package org.jboss.jsr299.tck.tests.implementation.simple.definition.constructorHasDisposesParameter;

import javax.enterprise.inject.Disposes;
import javax.inject.Inject;

class DisposingConstructor
{
   @Inject
   public DisposingConstructor(@Disposes Duck duck)
   {
      
   }
}
