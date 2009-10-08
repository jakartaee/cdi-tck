package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise;

import javax.ejb.Stateful;
import javax.inject.Named;


@Landowner
@Named
@Stateful
class Farmer implements FarmerLocal
{
   
   public String getClassName()
   {
      return Farmer.class.getName();
   }
   
}
