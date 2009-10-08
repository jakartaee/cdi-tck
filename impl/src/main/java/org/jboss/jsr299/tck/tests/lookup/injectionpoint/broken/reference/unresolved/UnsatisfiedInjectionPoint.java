package org.jboss.jsr299.tck.tests.lookup.injectionpoint.broken.reference.unresolved;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;

public class UnsatisfiedInjectionPoint implements InjectionPoint
{

   private final Bean<SimpleBean> bean;
   private final Set<Annotation> bindings = new HashSet<Annotation>();
   
   public UnsatisfiedInjectionPoint(Bean<SimpleBean> beanWithInjectionPoint)
   {
      this.bean = beanWithInjectionPoint;
      bindings.add(new DefaultLiteral());
      bindings.add(new AnyLiteral());
   }

   public Annotated getAnnotated()
   {
      return new AnnotatedInjectionField(this);
   }

   public Bean<?> getBean()
   {
      return bean;
   }

   public Set<Annotation> getQualifiers()
   {
      return bindings;
   }

   @SuppressWarnings("unchecked")
   public Member getMember()
   {
      return ((AnnotatedField<SimpleBean>)getAnnotated()).getJavaMember();
   }

   public Type getType()
   {
      return InjectedBean.class;
   }

   public boolean isDelegate()
   {
      return false;
   }

   public boolean isTransient()
   {
      return false;
   }

}
