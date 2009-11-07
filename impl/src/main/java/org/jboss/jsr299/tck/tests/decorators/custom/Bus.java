package org.jboss.jsr299.tck.tests.decorators.custom;

class Bus implements Vehicle
{

   public String start()
   {
      return "Bus started";
   }

   public String stop()
   {
      return "Bus stopped";
   }
}
