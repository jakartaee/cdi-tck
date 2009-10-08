package org.jboss.jsr299.tck.tests.context.dependent;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.decorator.Decorates;
import javax.decorator.Decorator;

@Decorator
class InteriorDecorator 
{
   @Decorates @Room Interior interior;
   
   private static List<InteriorDecorator> instances = new ArrayList<InteriorDecorator>();
   
   private static boolean destroyed;
   
   public static void reset()
   {
      instances.clear();
      destroyed = false;
   }
   
   public void foo()
   {
      instances.add(this);      
      interior.foo();
   }
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
   /**
    * @return the instances
    */
   public static List<InteriorDecorator> getInstances()
   {
      return instances;
   }
   
   /**
    * @return the destroyed
    */
   public static boolean isDestroyed()
   {
      return destroyed;
   }

}
