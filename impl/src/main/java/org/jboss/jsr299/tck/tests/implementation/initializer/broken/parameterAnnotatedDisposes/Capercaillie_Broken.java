package org.jboss.jsr299.tck.tests.implementation.initializer.broken.parameterAnnotatedDisposes;

import javax.enterprise.inject.Disposes;
import javax.inject.Inject;

class Capercaillie_Broken
{
   
   @Inject
   public void setName(String name, @Disposes ChickenHutch chickenHutch)
   {
      // No-op
   }
   
}
