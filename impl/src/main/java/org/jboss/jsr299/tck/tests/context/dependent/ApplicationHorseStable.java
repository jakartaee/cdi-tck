package org.jboss.jsr299.tck.tests.context.dependent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@ApplicationScoped
class ApplicationHorseStable
{
   @Inject
   private BeanManager beanManager;
   private static boolean dependentContextActive = false;
   
   public void horseEntered(@Observes HorseInStableEvent horseEvent)
   {
      dependentContextActive = beanManager.getContext(Dependent.class).isActive();
   }

   public static boolean isDependentContextActive()
   {
      return ApplicationHorseStable.dependentContextActive;
   }

   public static void setDependentContextActive(boolean dependentContextActive)
   {
      ApplicationHorseStable.dependentContextActive = dependentContextActive;
   }
}
