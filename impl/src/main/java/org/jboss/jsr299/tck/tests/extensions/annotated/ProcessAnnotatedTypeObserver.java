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
package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.util.HashSet;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension
{
   private static final HashSet<Class<?>> annotatedClasses = new HashSet<Class<?>>();
   private static AnnotatedType<Dog> dogAnnotatedType;

   public void observeAnnotatedType1(@Observes ProcessAnnotatedType<AbstractC> event)
   {
      annotatedClasses.add(event.getAnnotatedType().getJavaClass());
   }

   @SuppressWarnings("unchecked")
   public void observeAnnotatedTypes(@Observes ProcessAnnotatedType<?> event)
   {
      annotatedClasses.add(event.getAnnotatedType().getJavaClass());
      if (event.getAnnotatedType().getJavaClass().equals(Dog.class))
      {
         dogAnnotatedType = (AnnotatedType<Dog>) event.getAnnotatedType();
      }
      else if (event.getAnnotatedType().getJavaClass().equals(AbstractC.class))
      {
         // Ignore this one since the more specific observer above
         // should already process this.
      }
      else if (event.getAnnotatedType().getJavaClass().equals(VetoedBean.class))
      {
         event.veto();
      }
      else if (event.getAnnotatedType().getJavaClass().equals(ClassD.class))
      {
         wrapAnnotatedType(event);
      }
   }

   private <X> void wrapAnnotatedType(ProcessAnnotatedType<X> event)
   {
      event.setAnnotatedType(new TestAnnotatedType<X>(event.getAnnotatedType()));
   }
   
   public static HashSet<Class<?>> getAnnotatedclasses()
   {
      return annotatedClasses;
   }

   public static AnnotatedType<Dog> getDogAnnotatedType()
   {
      return dogAnnotatedType;
   }
}
