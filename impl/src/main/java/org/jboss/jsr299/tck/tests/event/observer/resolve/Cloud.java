package org.jboss.jsr299.tck.tests.event.observer.resolve;

import javax.enterprise.event.Observes;

class Cloud
{
   public void allocateNewDisk(@Observes DiskSpaceEvent event)
   {
   }
}
