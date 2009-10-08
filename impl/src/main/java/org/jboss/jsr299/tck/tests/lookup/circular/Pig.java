package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class Pig
{
   
   public static boolean success;
   
   @Inject Food food;
   
   public Pig()
   {
      success = false;
   }
   
   @PostConstruct
   public void postConstruct()
   {
      if (food.getName().equals("food"))
      {
         success = true;
      }
   }
   
   public String getName()
   {
      return "john";
   }
   
}
