package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.AnnotatedCallable;
import javax.enterprise.inject.spi.AnnotatedParameter;

class AnnotatedParameterWrapper<X> extends AnnotatedWrapper implements AnnotatedParameter<X>
{
   private AnnotatedParameter<X> delegate;
   
   public AnnotatedParameterWrapper(AnnotatedParameter<X> delegate, boolean keepOriginalAnnotations, Annotation... annotations)
   {
      super(delegate, keepOriginalAnnotations, annotations);
      this.delegate = delegate;
   }

   public AnnotatedCallable<X> getDeclaringCallable()
   {
      return delegate.getDeclaringCallable();
   }

   public int getPosition()
   {
      return delegate.getPosition();
   }

}
