package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class Space
{
   
   // For serialization
   public Space() {}
   
   @Inject
   public Space(Planet planet)
   {
   }
   
}
