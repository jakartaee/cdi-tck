package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;

@Stateful
public class Building implements BuildingLocal
{
   
   @Produces
   public Waste getWaste()
   {
      return new Waste(getClassName());
   }
   
   public String getClassName()
   {
      return Building.class.getName();
   }
   
}
