package org.jboss.jsr299.tck.tests.definition.bean.custom;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.jsr299.tck.literals.DefaultLiteral;

public class CatBean implements Bean<Cat>, Extension
{
   public static final CatBean bean = new CatBean();
   
   private boolean getBindingsCalled = false;
   private boolean getInjectionPointsCalled = false;
   private boolean getNameCalled = false;
   private boolean getScopeTypeCalled = false;
   private boolean getTypesCalled = false;
   private boolean isPolicyCalled = false;
   private boolean isSerializableCalled = false;
   private boolean isNullableCalled = false;
   private boolean isGetBeanClassCalled = false;
   private boolean getStereotypesCalled = false;
   

   @SuppressWarnings("serial")
   public Set<Annotation> getQualifiers()
   {
      getBindingsCalled = true;
      return new HashSet<Annotation>(){{ add(new DefaultLiteral());}};
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      getInjectionPointsCalled = true;
      return new HashSet<InjectionPoint>();
   }

   public String getName()
   {
      getNameCalled = true;
      return "cat";
   }
   
   public Set<Class<? extends Annotation>> getStereotypes() {
      getStereotypesCalled = true;
      return new HashSet<Class<? extends Annotation>>();
   }

   public Class<? extends Annotation> getScope()
   {
      getScopeTypeCalled = true;
      return Dependent.class;
   }

   @SuppressWarnings("serial")
   public Set<Type> getTypes()
   {
      getTypesCalled = true;
      return new HashSet<Type>() {{ add(Cat.class); add(Object.class); }};
   }

   public boolean isNullable()
   {
      isNullableCalled = true;
      return false;
   }

   public boolean isSerializable()
   {
      isSerializableCalled = true;
      return false;
   }
   
   public Class<?> getBeanClass()
   {
      isGetBeanClassCalled = true;
      return Cat.class;
   }
   
   public boolean isAlternative()
   {
      isPolicyCalled = true;
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

   public boolean isGetBindingsCalled()
   {
      return getBindingsCalled;
   }

   public boolean isGetInjectionPointsCalled()
   {
      return getInjectionPointsCalled;
   }

   public boolean isGetNameCalled()
   {
      return getNameCalled;
   }

   public boolean isGetScopeTypeCalled()
   {
      return getScopeTypeCalled;
   }

   public boolean isGetTypesCalled()
   {
      return getTypesCalled;
   }

   public boolean isPolicyCalled()
   {
      return isPolicyCalled;
   }

   public boolean isSerializableCalled()
   {
      return isSerializableCalled;
   }

   public boolean isNullableCalled()
   {
      return isNullableCalled;
   }
   
   public static CatBean getBean()
   {
      return bean;
   }

   public boolean isGetBeanClassCalled()
   {
      return isGetBeanClassCalled;
   }
   
   public boolean isGetStereotypesCalled()
   {
      return getStereotypesCalled;
   }

   public void afterDiscovery(@Observes AfterBeanDiscovery event) {
      event.addBean(bean);
   }
}
