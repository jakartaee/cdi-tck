package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;


@Specializes @Lazy
@Stateful
public class LazyFarmer extends Farmer implements LazyFarmerLocal, FarmerLocal
{
   
   @Override
   public String getClassName()
   {
      return LazyFarmer.class.getName();
   }
   
}
