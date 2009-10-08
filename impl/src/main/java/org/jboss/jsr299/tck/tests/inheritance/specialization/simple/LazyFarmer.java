package org.jboss.jsr299.tck.tests.inheritance.specialization.simple;

import javax.enterprise.inject.Specializes;


@Specializes @Lazy
class LazyFarmer extends Farmer
{
   
   @Override
   public String getClassName()
   {
      return LazyFarmer.class.getName();
   }
   
}
