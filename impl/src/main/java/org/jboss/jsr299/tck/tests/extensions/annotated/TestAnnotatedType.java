package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

public class TestAnnotatedType<X> implements AnnotatedType<X>
{
   private AnnotatedType<X> delegate;
   private static boolean getConstructorsUsed = false;
   private static boolean getFieldsUsed = false;
   private static boolean getMethodsUsed = false;
   
   public TestAnnotatedType(AnnotatedType<X> delegate)
   {
      this.delegate = delegate;
   }

   public Set<AnnotatedConstructor<X>> getConstructors()
   {
      getConstructorsUsed = true;
      return delegate.getConstructors();
   }

   public Set<AnnotatedField<? super X>> getFields()
   {
      getFieldsUsed = true;
      return delegate.getFields();
   }

   public Class<X> getJavaClass()
   {
      return delegate.getJavaClass();
   }

   public Set<AnnotatedMethod<? super X>> getMethods()
   {
      getMethodsUsed = true;
      return delegate.getMethods();
   }

   public <T extends Annotation> T getAnnotation(Class<T> annotationType)
   {
      return delegate.getAnnotation(annotationType);
   }

   public Set<Annotation> getAnnotations()
   {
      return delegate.getAnnotations();
   }

   public Type getBaseType()
   {
      return delegate.getBaseType();
   }

   public Set<Type> getTypeClosure()
   {
      return delegate.getTypeClosure();
   }

   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
   {
      return delegate.isAnnotationPresent(annotationType);
   }

   public static boolean isGetConstructorsUsed()
   {
      return getConstructorsUsed;
   }

   public static boolean isGetFieldsUsed()
   {
      return getFieldsUsed;
   }

   public static boolean isGetMethodsUsed()
   {
      return getMethodsUsed;
   }

}
