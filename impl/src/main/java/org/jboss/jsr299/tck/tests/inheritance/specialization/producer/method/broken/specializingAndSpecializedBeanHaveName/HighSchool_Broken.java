package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.specializingAndSpecializedBeanHaveName;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Named;

class HighSchool_Broken extends School
{
   @Override @Produces @Specializes@Named
   public Pupil getStarPupil()
   {
      return super.getStarPupil();
   }
   
}
