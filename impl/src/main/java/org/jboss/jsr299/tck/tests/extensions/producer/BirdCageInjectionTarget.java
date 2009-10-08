package org.jboss.jsr299.tck.tests.extensions.producer;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

class BirdCageInjectionTarget implements InjectionTarget<BirdCage>
{
   private InjectionTarget<BirdCage> wrappedInjectionTarget;
   private static boolean injectCalled;

   public BirdCageInjectionTarget(InjectionTarget<BirdCage> originalInjectionTarget)
   {
      this.wrappedInjectionTarget = originalInjectionTarget;
   }

   public void inject(BirdCage instance, CreationalContext<BirdCage> ctx)
   {
      injectCalled = true;
      wrappedInjectionTarget.inject(instance, ctx);
   }

   public void postConstruct(BirdCage instance)
   {
      wrappedInjectionTarget.postConstruct(instance);
   }

   public void preDestroy(BirdCage instance)
   {
      wrappedInjectionTarget.preDestroy(instance);
   }

   public void dispose(BirdCage instance)
   {
      wrappedInjectionTarget.dispose(instance);
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return wrappedInjectionTarget.getInjectionPoints();
   }

   public BirdCage produce(CreationalContext<BirdCage> ctx)
   {
      return wrappedInjectionTarget.produce(ctx);
   }

   public static boolean isInjectCalled()
   {
      return injectCalled;
   }

   public static void setInjectCalled(boolean injectCalled)
   {
      BirdCageInjectionTarget.injectCalled = injectCalled;
   }

}
