package org.jboss.jsr299.tck.tests.extensions.producer;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

class CheckableInjectionTarget<T> implements InjectionTarget<T>
{
   private InjectionTarget<T> wrappedInjectionTarget;
   private static boolean injectCalled;

   public CheckableInjectionTarget(InjectionTarget<T> originalInjectionTarget)
   {
      this.wrappedInjectionTarget = originalInjectionTarget;
   }

   public void inject(T instance, CreationalContext<T> ctx)
   {
      injectCalled = true;
      wrappedInjectionTarget.inject(instance, ctx);
   }

   public void postConstruct(T instance)
   {
      wrappedInjectionTarget.postConstruct(instance);
   }

   public void preDestroy(T instance)
   {
      wrappedInjectionTarget.preDestroy(instance);
   }

   public void dispose(T instance)
   {
      wrappedInjectionTarget.dispose(instance);
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return wrappedInjectionTarget.getInjectionPoints();
   }

   public T produce(CreationalContext<T> ctx)
   {
      return wrappedInjectionTarget.produce(ctx);
   }

   public static boolean isInjectCalled()
   {
      return injectCalled;
   }

   public static void setInjectCalled(boolean injectCalled)
   {
      CheckableInjectionTarget.injectCalled = injectCalled;
   }

}
