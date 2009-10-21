package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;

class AnnotatedConstructorWrapper<X> extends AnnotatedWrapper implements AnnotatedConstructor<X>
{
   
   private AnnotatedConstructor<X> delegate;
   
   public AnnotatedConstructorWrapper(AnnotatedConstructor<X> delegate, boolean keepOriginalAnnotations, Annotation... annotations)
   {
      super(delegate, keepOriginalAnnotations, annotations);
      this.delegate = delegate;
   }

   public Constructor<X> getJavaMember()
   {
      return delegate.getJavaMember();
   }

   public List<AnnotatedParameter<X>> getParameters()
   {
      return delegate.getParameters();
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
