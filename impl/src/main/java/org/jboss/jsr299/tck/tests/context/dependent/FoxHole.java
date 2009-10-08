package org.jboss.jsr299.tck.tests.context.dependent;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

class FoxHole
{
   @Inject public Fox fox;
   
   public Fox initializerFox;
   
   @Inject
   public void init(Fox fox)
   {
      this.initializerFox = fox;
   }
}
