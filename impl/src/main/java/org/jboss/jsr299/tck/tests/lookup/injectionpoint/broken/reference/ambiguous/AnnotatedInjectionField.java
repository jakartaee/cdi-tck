package org.jboss.jsr299.tck.tests.lookup.injectionpoint.broken.reference.ambiguous;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.InjectionPoint;

class AnnotatedInjectionField implements AnnotatedField<InjectedBean>
{

   private final InjectionPoint injectionPoint;

   public AnnotatedInjectionField(InjectionPoint injectionPoint)
   {
      this.injectionPoint = injectionPoint;
   }

   public Field getJavaMember()
   {
      try
      {
         return SimpleBean.class.getDeclaredField("injectedBean");
      }
      catch (Exception e)
      {
         throw new RuntimeException("Failed to get field for injectedBean", e);
      }
   }

   public AnnotatedType<InjectedBean> getDeclaringType()
   {
      return null;
   }

   public boolean isStatic()
   {
      return false;
   }

   public <T extends Annotation> T getAnnotation(Class<T> annotationType)
   {
      return null;
   }

   public Set<Annotation> getAnnotations()
   {
      return null;
   }

   public Type getBaseType()
   {
      return InjectedBean.class;
   }

   public Set<Type> getTypeClosure()
   {
      return null;
   }

   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
   {
      return false;
   }

}
