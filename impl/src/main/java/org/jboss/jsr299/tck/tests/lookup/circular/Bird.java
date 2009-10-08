package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@SuppressWarnings("unused")
class Bird
{
   
   private Air air;
   
   public Bird()
   {
      
   }
   
   @Inject
   public Bird(Air air)
   {
      this.air = air;
   }
   
}
