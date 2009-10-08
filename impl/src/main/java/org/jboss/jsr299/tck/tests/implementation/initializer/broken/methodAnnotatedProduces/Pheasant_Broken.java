package org.jboss.jsr299.tck.tests.implementation.initializer.broken.methodAnnotatedProduces;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

class Pheasant_Broken
{
 
   @Inject
   @Produces
   public void setName(String name)
   {
      // No-op
   }
   
}
