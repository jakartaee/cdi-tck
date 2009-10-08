package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Current;

class Farm
{
   
   public Date founded;
   public Date closed;
   public int initialStaff;
   public String location;
   
   @Current
   FarmOffice farmOffice;
   
   @PostConstruct
   protected void postConstruct() 
   {
      founded = new Date();
      initialStaff = farmOffice.noOfStaff;
   }
   
   @PreDestroy
   protected void preDestroy() 
   {
      closed = new Date();
   }
   
}
