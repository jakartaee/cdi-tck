package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

class SpiderProducer
{
   @Inject
   private BeanManager beanManager;
   
   private static boolean dependentContextActive = false;
   private static boolean destroyed = false;
   private static SpiderProducer instanceUsedForDisposal = null;


   @Produces @Pet public Tarantula produceTarantula(Tarantula spider)
   {
      dependentContextActive = beanManager.getContext(Dependent.class).isActive();
      return spider;
   }

   public void disposeTarantula(@Disposes @Pet Tarantula tarantula, Fox fox)
   {
      dependentContextActive = beanManager.getContext(Dependent.class).isActive();
      instanceUsedForDisposal = this;
   }

   public static boolean isDependentContextActive()
   {
      return dependentContextActive;
   }

   public static SpiderProducer getInstanceUsedForDisposal()
   {
      return instanceUsedForDisposal;
   }
   
   public static boolean isDestroyed()
   {
      return destroyed;
   }

   public static void reset()
   {
     destroyed = false;
     dependentContextActive = false;
     instanceUsedForDisposal = null;
   }
   
   @PreDestroy
   public void destroy()
   {
      destroyed = true;
   }
}
