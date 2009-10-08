package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

class Car
{
   
   public static boolean success;
   
   @Inject Petrol petrol;
   
   public Car()
   {
      success = false;
   }
   
   @PostConstruct
   public void postConstruct()
   {
      if (petrol.getName().equals("petrol"))
      {
         success = true;
      }
   }
   
   public String getName()
   {
      return "herbie";
   }
   
}
