package org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.two;

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
