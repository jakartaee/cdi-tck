package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.AnnotatedCallable;
import javax.enterprise.inject.spi.AnnotatedParameter;

class TestAnnotatedParameter<X> extends TestAnnotated implements AnnotatedParameter<X>
{
   private AnnotatedParameter<X> delegate;
   
   public TestAnnotatedParameter(AnnotatedParameter<X> delegate, Annotation... annotations)
   {
      super(delegate, annotations);
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
