package org.jboss.jsr299.tck.tests.event.observer.resolve;

class Temperature
{
   private double degrees;

   public Temperature(double degrees)
   {
      this.degrees = degrees;
   }

   public double getDegrees()
   {
      return degrees;
   }

}
