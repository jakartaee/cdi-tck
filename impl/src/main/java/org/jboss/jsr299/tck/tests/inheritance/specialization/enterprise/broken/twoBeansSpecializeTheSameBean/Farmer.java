package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.twoBeansSpecializeTheSameBean;

import javax.ejb.Stateful;
import javax.inject.Named;

@Landowner
@Named
@Stateful
class Farmer implements FarmerInterface
{
   public String getClassName()
   {
      return Farmer.class.getName();
   }
}
