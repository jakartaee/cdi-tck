package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.util.HashSet;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension
{
   private static final HashSet<Class<?>> annotatedClasses = new HashSet<Class<?>>();
   private static AnnotatedType<Dog> dogAnnotatedType;

   public void observeAnnotatedType1(@Observes ProcessAnnotatedType<AbstractC> event)
   {
      annotatedClasses.add(event.getAnnotatedType().getJavaClass());
   }

   @SuppressWarnings("unchecked")
   public void observeAnnotatedTypes(@Observes ProcessAnnotatedType<?> event)
   {
      annotatedClasses.add(event.getAnnotatedType().getJavaClass());
      if (event.getAnnotatedType().getJavaClass().equals(Dog.class))
      {
         dogAnnotatedType = (AnnotatedType<Dog>) event.getAnnotatedType();
      }
      else if (event.getAnnotatedType().getJavaClass().equals(AbstractC.class))
      {
         // Ignore this one since the more specific observer above
         // should already process this.
      }
      else if (event.getAnnotatedType().getJavaClass().equals(VetoedBean.class))
      {
         event.veto();
      }
      else if (event.getAnnotatedType().getJavaClass().equals(ClassD.class))
      {
         wrapAnnotatedType(event);
      }
   }

   private <X> void wrapAnnotatedType(ProcessAnnotatedType<X> event)
   {
      event.setAnnotatedType(new TestAnnotatedType<X>(event.getAnnotatedType()));
   }
   
   public static HashSet<Class<?>> getAnnotatedclasses()
   {
      return annotatedClasses;
   }

   public static AnnotatedType<Dog> getDogAnnotatedType()
   {
      return dogAnnotatedType;
   }
}
