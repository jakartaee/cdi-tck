package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.Typed;

@Typed(FlightlessBird.class)
public class Emu extends FlightlessBird<Australian>
{
   public Emu()
   {
      super("Emu");
   }
}
