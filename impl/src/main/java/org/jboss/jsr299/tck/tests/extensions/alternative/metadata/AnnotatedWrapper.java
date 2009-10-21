package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.Annotated;

class AnnotatedWrapper implements Annotated
{
   
   private Annotated delegate;
   private Set<Annotation> annotations;
   
   public AnnotatedWrapper(Annotated delegate, boolean keepOriginalAnnotations, Annotation... annotations)
   {
      this.delegate = delegate;
      this.annotations = new HashSet<Annotation>(Arrays.asList(annotations));
      if (keepOriginalAnnotations) 
      {
         this.annotations.addAll(delegate.getAnnotations());
      }
   }

   @SuppressWarnings("unchecked")
   public <T extends Annotation> T getAnnotation(Class<T> arg0)
   {
      for (Annotation annotation : annotations) {
         if (arg0.isAssignableFrom(annotation.annotationType())) {
            return (T) annotation;
         }
      }
      return null;
   }

   public Set<Annotation> getAnnotations()
   {
         return Collections.unmodifiableSet(annotations);
   }

   public Type getBaseType()
   {
      return delegate.getBaseType();
   }

   public Set<Type> getTypeClosure()
   {
      return delegate.getTypeClosure();
   }

   public boolean isAnnotationPresent(Class<? extends Annotation> arg0)
   {
      for (Annotation annotation : annotations) {
         if (arg0.isAssignableFrom(annotation.annotationType())) {
            return true;
         }
      }
      return false;
   }
}
