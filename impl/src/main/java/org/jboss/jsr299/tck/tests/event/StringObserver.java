package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.event.Observes;

class StringObserver
{
   public void anotherObserver(@Observes String event)
   {
      
   }
}
