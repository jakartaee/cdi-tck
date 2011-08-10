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

public class CatBean implements Bean<Cat>, PassivationCapable
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
