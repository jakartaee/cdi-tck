package org.jboss.jsr299.tck.tests.context;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

class MyContextual implements Bean<MySessionBean>
{
   private boolean createCalled = false;
   private boolean destroyCalled = false;
   private boolean shouldReturnNullInstances = false;

   protected MyContextual(BeanManager beanManager)
   {
   }

   public Set<Annotation> getQualifiers()
   {
      return Collections.emptySet();
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return Collections.emptySet();
   }

   public String getName()
   {
      return "my-session-bean";
   }

   public Class<? extends Annotation> getScope()
   {
      return SessionScoped.class;
   }

   @SuppressWarnings("unchecked")
   public Set<Type> getTypes()
   {
      return new HashSet<Type>(Arrays.asList(Object.class, MySessionBean.class, Serializable.class));
   }

   public boolean isNullable()
   {
      return false;
   }

   public MySessionBean create(CreationalContext<MySessionBean> creationalContext)
   {
      createCalled = true;
      if (shouldReturnNullInstances)
         return null;
      else
         return new MySessionBean();
   }

   public void destroy(MySessionBean instance, CreationalContext<MySessionBean> creationalContext)
   {
      destroyCalled = true;
   }

   public boolean isCreateCalled()
   {
      return createCalled;
   }

   public boolean isDestroyCalled()
   {
      return destroyCalled;
   }

   public void setShouldReturnNullInstances(boolean shouldReturnNullInstances)
   {
      this.shouldReturnNullInstances = shouldReturnNullInstances;
   }

   public Class<?> getBeanClass()
   {
      return MySessionBean.class;
   }

   public boolean isAlternative()
   {
      return false;
   }

   public Set<Class<? extends Annotation>> getStereotypes()
   {
      return Collections.emptySet();
   }

}
