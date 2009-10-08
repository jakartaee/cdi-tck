package org.jboss.jsr299.tck.tests.event.observer.resolve;

import javax.enterprise.event.Observes;

public class AirConditioner
{
   private Temperature target;

   private boolean on = false;
   
   public void setTargetTemperature(Temperature target)
   {
      this.target = target;
   }
   
   public void temperatureChanged(@Observes Temperature temperature)
   {
      if (on && temperature.getDegrees() <= target.getDegrees())
      {
         on = false;
      }
      else if (!on && temperature.getDegrees() > target.getDegrees())
      {
         on = true;
      }
   }
   
   public boolean isOn()
   {
      return on;
   }
}
