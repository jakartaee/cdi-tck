package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

@Stateful
@RequestScoped
class GoldenRetriever implements GoldenRetrieverLocal
{
   @Remove
   public void bye(Object something) {
      
   }

   public void anObserverMethod(@Observes String event)
   {
      
   }
}
