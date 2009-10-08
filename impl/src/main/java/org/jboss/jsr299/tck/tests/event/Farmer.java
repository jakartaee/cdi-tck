package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.event.Observes;

class Farmer
{
   private static Class<?> observerClazz;
   
   public void observeEggLaying(@Observes Egg egg)
   {
      observerClazz = this.getClass();
      egg.recordVisit(this);
   }

   public static Class<?> getObserverClazz()
   {
      return observerClazz;
   }
   
}
