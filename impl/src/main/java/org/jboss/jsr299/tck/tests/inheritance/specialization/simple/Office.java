package org.jboss.jsr299.tck.tests.inheritance.specialization.simple;

import javax.enterprise.inject.Specializes;

@Specializes
class Office extends Building
{
   
   @Override
   protected String getClassName()
   {
      return Office.class.getName();
   }
   
}
