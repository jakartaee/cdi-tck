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
package org.jboss.jsr299.tck.tests.context.dependent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@ApplicationScoped
public class ApplicationHorseStable
{
   @Inject
   private BeanManager beanManager;
   private static boolean dependentContextActive = false;
   
   public void horseEntered(@Observes HorseInStableEvent horseEvent)
   {
      dependentContextActive = beanManager.getContext(Dependent.class).isActive();
   }

   public static boolean isDependentContextActive()
   {
      return ApplicationHorseStable.dependentContextActive;
   }

   public static void setDependentContextActive(boolean dependentContextActive)
   {
      ApplicationHorseStable.dependentContextActive = dependentContextActive;
   }
}
