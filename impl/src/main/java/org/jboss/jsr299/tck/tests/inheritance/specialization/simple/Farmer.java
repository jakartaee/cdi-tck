package org.jboss.jsr299.tck.tests.inheritance.specialization.simple;

import javax.inject.Named;


@Landowner
@Named
class Farmer
{
   
   public String getClassName()
   {
      return Farmer.class.getName();
   }
   
}
