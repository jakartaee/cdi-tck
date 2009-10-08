package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class Air
{
   
   public Air()
   {
   
   }
   
   @Inject
   public Air(Bird bird)
   {
   }
   
}
