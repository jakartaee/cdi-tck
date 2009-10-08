package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class House
{
   
   // For serialization
   public House() {}
   
   @Inject
   public House(House house)
   {
      house.ping();
   }
   
   private void ping()
   {
      
   }
   
}
