package org.jboss.jsr299.tck.tests.lookup.injection.nullableBean;

import javax.enterprise.inject.Produces;

class FarmHouseProducer_Broken
{
   
   @Produces public Integer getNumberOfBedrooms()
   {
      return null;
   }
   
}
