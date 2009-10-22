package org.jboss.jsr299.tck.tests.extensions.container.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension
{
   private static ProcessAnnotatedType<Sheep> statelessSessionBeanEvent = null;
   private static ProcessAnnotatedType<Cow> statefulSessionBeanEvent = null;
   private static ProcessAnnotatedType<SheepInterceptor> sessionBeanInterceptorEvent = null;
   private static ProcessAnnotatedType<Farm> managedBeanEvent = null;
   
   public void observeStatelessSessionBean(@Observes ProcessAnnotatedType<Sheep> event) {
      statelessSessionBeanEvent = event;
   }
   
   public void observeStatefulSessionBean(@Observes ProcessAnnotatedType<Cow> event) {
      statefulSessionBeanEvent = event;
   }
   
   public void observeSessionBeanInterceptor(@Observes ProcessAnnotatedType<SheepInterceptor> event) {
      sessionBeanInterceptorEvent = event;
   }
   
   public void observeManagedBean(@Observes ProcessAnnotatedType<Farm> event) {
      managedBeanEvent = event;
   }

   public static ProcessAnnotatedType<Sheep> getStatelessSessionBeanEvent()
   {
      return statelessSessionBeanEvent;
   }

   public static ProcessAnnotatedType<Cow> getStatefulSessionBeanEvent()
   {
      return statefulSessionBeanEvent;
   }

   public static ProcessAnnotatedType<SheepInterceptor> getSessionBeanInterceptorEvent()
   {
      return sessionBeanInterceptorEvent;
   }

   public static ProcessAnnotatedType<Farm> getManagedBeanEvent()
   {
      return managedBeanEvent;
   }
}
