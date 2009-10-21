package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;

class AnnotatedMethodWrapper<X> extends AnnotatedWrapper implements AnnotatedMethod<X>
{

   private AnnotatedMethod<X> delegate;
   
   public AnnotatedMethodWrapper(AnnotatedMethod<X> delegate, boolean keepOriginalAnnotations, Annotation... annotations) 
   {
      super(delegate, keepOriginalAnnotations, annotations);
      this.delegate = delegate;
   }

   public Method getJavaMember()
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

   @Override
   public Set<Annotation> getAnnotations()
   {
      // TODO Auto-generated method stub
      return super.getAnnotations();
   }
   
   
   
}
