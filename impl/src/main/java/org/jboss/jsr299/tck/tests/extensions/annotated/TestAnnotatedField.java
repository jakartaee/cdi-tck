package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;

class TestAnnotatedField<X> extends TestAnnotated implements AnnotatedField<X>
{
   
   private AnnotatedField<X> delegate;
   
   public TestAnnotatedField(AnnotatedField<X> delegate, Annotation... annotations)
   {
      super(delegate, annotations);
      this.delegate = delegate;
   }
   
   public Field getJavaMember()
   {
      return delegate.getJavaMember();
   }

   public AnnotatedType<X> getDeclaringType()
   {
      return delegate.getDeclaringType();
   }

   public boolean isStatic()
   {
      return delegate.isStatic();
   }
}
