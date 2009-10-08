package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.inject.Specializes;

@Stateful
@Specializes
public class Office extends Building implements OfficeLocal
{
   
   @Override
   public String getClassName()
   {
      return Office.class.getName();
   }
   
}
