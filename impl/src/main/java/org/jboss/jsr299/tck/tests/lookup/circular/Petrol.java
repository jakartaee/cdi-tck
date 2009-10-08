package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class Petrol
{
   
   public static boolean success;
   
   @Inject Car car;
   
   public Petrol()
   {
      success = false;
   }
   
   @PostConstruct
   public void postConstruct()
   {
      if (car.getName().equals("herbie"))
      {
         success = true;
      }
   }
   
   public String getName()
   {
      return "petrol";
   }
   
}
