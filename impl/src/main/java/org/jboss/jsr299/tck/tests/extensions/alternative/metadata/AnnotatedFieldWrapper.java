package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;

class AnnotatedFieldWrapper<X> extends AnnotatedWrapper implements AnnotatedField<X>
{
   
   private AnnotatedField<X> delegate;
   
   public AnnotatedFieldWrapper(AnnotatedField<X> delegate, boolean keepOriginalAnnotations, Annotation... annotations)
   {
      super(delegate, keepOriginalAnnotations, annotations);
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
