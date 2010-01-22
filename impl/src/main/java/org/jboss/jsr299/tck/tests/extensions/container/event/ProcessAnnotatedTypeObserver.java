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
package org.jboss.jsr299.tck.tests.extensions.container.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension
{
   private static ProcessAnnotatedType<Sheep> statelessSessionBeanEvent = null;
   private static ProcessAnnotatedType<Cow> statefulSessionBeanEvent = null;
   private static ProcessAnnotatedType<SheepInterceptor> sessionBeanInterceptorEvent = null;
   private static ProcessAnnotatedType<Farm> managedBeanEvent = null;
   
   public void cleanup(@Observes BeforeShutdown shutdown)
   {
      statefulSessionBeanEvent = null;
      statelessSessionBeanEvent = null;
      sessionBeanInterceptorEvent = null;
      managedBeanEvent = null;
   }
   
   public void observeStatelessSessionBean(@Observes ProcessAnnotatedType<Sheep> event) {
      statelessSessionBeanEvent = event;
   }
   
   public void observeStatefulSessionBean(@Observes ProcessAnnotatedType<Cow> event) {
      statefulSessionBeanEvent = event;
   }
   
   public void observeSessionBeanInterceptor(@Observes ProcessAnnotatedType<SheepInterceptor> event) {
      sessionBeanInterceptorEvent = event;
   }
   
   public void observeManagedBean(@Observes ProcessAnnotatedType<Farm> event) {
      managedBeanEvent = event;
   }

   public static ProcessAnnotatedType<Sheep> getStatelessSessionBeanEvent()
   {
      return statelessSessionBeanEvent;
   }

   public static ProcessAnnotatedType<Cow> getStatefulSessionBeanEvent()
   {
      return statefulSessionBeanEvent;
   }

   public static ProcessAnnotatedType<SheepInterceptor> getSessionBeanInterceptorEvent()
   {
      return sessionBeanInterceptorEvent;
   }

   public static ProcessAnnotatedType<Farm> getManagedBeanEvent()
   {
      return managedBeanEvent;
   }
}
