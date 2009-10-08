package org.jboss.jsr299.tck.tests.inheritance.specialization.simple;

import javax.enterprise.inject.Produces;

class Building
{
   
   protected String getClassName()
   {
      return Building.class.getName();
   }
   
   @Produces
   public Waste getWaste()
   {
      return new Waste(getClassName());
   }
   
}
