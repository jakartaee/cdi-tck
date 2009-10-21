package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.jsr299.tck.tests.extensions.alternative.metadata.AnnotatedWrapper;

public class AnnotatedTypeWrapper<X> extends AnnotatedWrapper implements AnnotatedType<X>
{
   private AnnotatedType<X> delegate;
   
   public AnnotatedTypeWrapper(AnnotatedType<X> delegate, boolean keepOriginalAnnotations, Annotation...annotations)
   {
      super(delegate, keepOriginalAnnotations, annotations);
      this.delegate = delegate;
   }

   public Set<AnnotatedConstructor<X>> getConstructors()
   {
      return delegate.getConstructors();
   }

   public Set<AnnotatedField<? super X>> getFields()
   {
      return delegate.getFields();
   }

   public Class<X> getJavaClass()
   {
      return delegate.getJavaClass();
   }

   public Set<AnnotatedMethod<? super X>> getMethods()
   {
      return delegate.getMethods();
   }
}
