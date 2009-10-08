package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
class Food
{
   
   public static boolean success;
   
   @Inject Pig pig;
   
   public Food()
   {
      success = false;
   }
   
   @PostConstruct
   public void postConstruct()
   {
      if (pig.getName().equals("john"))
      {
         success = true;
      }
   }
   
   public String getName()
   {
      return "food";
   }
   
}
