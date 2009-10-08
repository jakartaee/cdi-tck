package org.jboss.jsr299.tck.tests.extensions.beanManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;

import org.jboss.jsr299.tck.literals.DefaultLiteral;

class CatBean implements Bean<Cat>, PassivationCapable
{
   public static final CatBean bean = new CatBean();
   
   @SuppressWarnings("serial")
   public Set<Annotation> getQualifiers()
   {
      return new HashSet<Annotation>(){{ add(new DefaultLiteral());}};
   }

   public Class<? extends Annotation> getDeploymentType()
   {
      return null;
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return new HashSet<InjectionPoint>();
   }

   public String getName()
   {
      return "cat";
   }
   
   public Set<Class<? extends Annotation>> getStereotypes() {
      return new HashSet<Class<? extends Annotation>>();
   }

   public Class<? extends Annotation> getScope()
   {
      return Dependent.class;
   }

   @SuppressWarnings("serial")
   public Set<Type> getTypes()
   {
      return new HashSet<Type>() {{ add(Cat.class); add(Object.class); }};
   }

   public boolean isNullable()
   {
      return false;
   }

   public boolean isSerializable()
   {
      return false;
   }
   
   public Class<?> getBeanClass()
   {
      return Cat.class;
   }
   
   public boolean isAlternative()
   {
      return false;
   }

   public Cat create(CreationalContext<Cat> creationalContext)
   {
      return new Cat("kitty");
   }

   public void destroy(Cat instance, CreationalContext<Cat> creationalContext)
   {
      creationalContext.release();
   }

   public static CatBean getBean()
   {
      return bean;
   }

   public void afterDiscovery(@Observes AfterBeanDiscovery event) {
      event.addBean(bean);
   }

   public String getId()
   {
      return "org.jboss.jsr299.tck.tests.extensions.beanManager.CatBean";
   }
}
