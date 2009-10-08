package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.specializingAndSpecializedBeanHaveName;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

class School
{
   
   @Produces @Named("bestPupil")
   public Pupil getStarPupil()
   {
      return new Pupil();
   }
    
   
}
