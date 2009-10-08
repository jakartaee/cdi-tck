package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@Dependent
class HorseStable
{
   private static boolean dependentContextActive = false;
   private static HorseStable instanceThatObservedEvent = null;
   private static boolean destroyed = false;

   @Inject
   public HorseStable(BeanManager beanManager)
   {
      dependentContextActive = beanManager.getContext(Dependent.class).isActive();
   }

   public void horseEntered(@Observes HorseInStableEvent horseEvent, Fox fox)
   {
      instanceThatObservedEvent = this;
   }
   
   @PreDestroy
   public void destroy()
   {
      destroyed = true;
   }

   public static boolean isDependentContextActive()
   {
      return dependentContextActive;
   }

   public static void reset()
   {
      HorseStable.dependentContextActive = false;
      instanceThatObservedEvent = null;
      destroyed = false;
   }

   public static HorseStable getInstanceThatObservedEvent()
   {
      return instanceThatObservedEvent;
   }
   
   public static boolean isDestroyed()
   {
      return destroyed;
   }
}
