package org.jboss.jsr299.tck.tests.extensions.beanManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

class InjectionPointDecorator implements InjectionPoint
{
   private final InjectionPoint injectionPoint;
   
   public InjectionPointDecorator(InjectionPoint injectionPoint)
   {
      this.injectionPoint = injectionPoint;
   }

   public Annotated getAnnotated()
   {
      return injectionPoint.getAnnotated();
   }

   public Bean<?> getBean()
   {
      return injectionPoint.getBean();
   }

   public Set<Annotation> getQualifiers()
   {
      return injectionPoint.getQualifiers();
   }

   public Member getMember()
   {
      return injectionPoint.getMember();
   }

   public Type getType()
   {
      return Dog.class;
   }

   public boolean isDelegate()
   {
      return injectionPoint.isDelegate();
   }

   public boolean isTransient()
   {
      return injectionPoint.isTransient();
   }
}
