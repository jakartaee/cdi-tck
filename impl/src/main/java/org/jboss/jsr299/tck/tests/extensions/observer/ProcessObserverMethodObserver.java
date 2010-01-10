package org.jboss.jsr299.tck.tests.extensions.observer;

import java.lang.reflect.Type;
import java.util.HashSet;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessObserverMethod;

public class ProcessObserverMethodObserver implements Extension
{
   private static final HashSet<Type>  eventTypes = new HashSet<Type>();
   private static AnnotatedMethod<?>   annotatedMethod;
   private static ObserverMethod<?> observerMethod;
   
   public void cleanup(@Observes BeforeShutdown shutdown)
   {
      annotatedMethod = null;
      observerMethod = null;
   }
   
   public void observeObserverMethodForEventA(@Observes ProcessObserverMethod<EventA, EventAObserver> event)
   {
      eventTypes.add(event.getObserverMethod().getObservedType());
      annotatedMethod = event.getAnnotatedMethod();
      observerMethod = event.getObserverMethod();
   }

   public static HashSet<Type> getEventtypes()
   {
      return eventTypes;
   }

   public static AnnotatedMethod<?> getAnnotatedMethod()
   {
      return annotatedMethod;
   }

   public static ObserverMethod<?> getObserverMethod()
   {
      return observerMethod;
   }
}
