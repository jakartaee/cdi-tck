package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

public class TestAnnotatedType<X> extends TestAnnotated implements AnnotatedType<X>
{
   private AnnotatedType<X> delegate;
   private static boolean getConstructorsUsed = false;
   private static boolean getFieldsUsed = false;
   private static boolean getMethodsUsed = false;
   
   public TestAnnotatedType(AnnotatedType<X> delegate, Annotation...annotations)
   {
      super(delegate, annotations);
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
