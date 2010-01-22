/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

public abstract class ForwardingBean<T> implements Bean<T>
{
   
   protected ForwardingBean()
   {
   }

   protected abstract Bean<T> delegate();

   public Set<Annotation> getQualifiers()
   {
      return delegate().getQualifiers();
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return delegate().getInjectionPoints();
   }

   public String getName()
   {
      return delegate().getName();
   }

   public Class<? extends Annotation> getScope()
   {
      return delegate().getScope();
   }

   public Set<Type> getTypes()
   {
      return delegate().getTypes();
   }

   public boolean isNullable()
   {
      return delegate().isNullable();
   }

   public T create(CreationalContext<T> creationalContext)
   {
      return delegate().create(creationalContext);
   }

   public void destroy(T instance, CreationalContext<T> creationalContext)
   {
      delegate().destroy(instance, creationalContext);
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
