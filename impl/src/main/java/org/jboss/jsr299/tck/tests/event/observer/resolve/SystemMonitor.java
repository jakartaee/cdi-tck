package org.jboss.jsr299.tck.tests.event.observer.resolve;

import javax.enterprise.event.Observes;

class SystemMonitor
{
   public void lowBattery(@Observes BatteryEvent e)
   {
   }
   
   public void lowDiskSpace(@Observes DiskSpaceEvent e)
   {
   }
}
