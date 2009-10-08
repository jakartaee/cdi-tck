package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.annotation.PreDestroy;


class FarmOffice
{
   
   public int noOfStaff = 20;
   
   @PreDestroy
   public void preDestroy()
   {
      noOfStaff = 0;
   }
}
