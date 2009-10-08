package org.jboss.jsr299.tck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.context.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.inject.manager.InjectionPoint;
import javax.inject.manager.Manager;

public abstract class ForwardingBean<T> implements Bean<T>
{
   
   protected ForwardingBean(Manager manager)
   {
      super(manager);
   }

   protected abstract Bean<T> delegate();

   @Override
   public Set<Annotation> getBindings()
   {
      return delegate().getBindings();
   }

   @Override
   public Class<? extends Annotation> getDeploymentType()
   {
      return delegate().getDeploymentType();
   }

   @Override
   public Set<? extends InjectionPoint> getInjectionPoints()
   {
      return delegate().getInjectionPoints();
   }

   @Override
   public String getName()
   {
      return delegate().getName();
   }

   @Override
   public Class<? extends Annotation> getScopeType()
   {
      return delegate().getScopeType();
   }

   @Override
   public Set<Type> getTypes()
   {
      return delegate().getTypes();
   }

   @Override
   public boolean isNullable()
   {
      return delegate().isNullable();
   }

   @Override
   public boolean isSerializable()
   {
      return delegate().isSerializable();
   }

   public T create(CreationalContext<T> creationalContext)
   {
      return delegate().create(creationalContext);
   }

   public void destroy(T instance)
   {
      delegate().destroy(instance);
   }
   
   @Override
   public boolean equals(Object obj)
   {
      return delegate().equals(obj);
   }
   
   @Override
   public String toString()
   {
      return delegate().toString();
   }
   
   @Override
   public int hashCode()
   {
      return delegate().hashCode();
   }
}
