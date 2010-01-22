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
package org.jboss.jsr299.tck.tests.event.bindingTypes;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class DiscerningObserver
{
   private int numTimesAnyBindingTypeEventObserved = 0;
   private int numTimesNonRuntimeBindingTypeObserved = 0;

   public void observeAny(@Observes @Any String event)
   {
      numTimesAnyBindingTypeEventObserved++;
   }
   
   public void observeNonRuntime(@Observes @Any @NonRuntimeBindingType String event)
   {
      numTimesNonRuntimeBindingTypeObserved++;
   }
   
   public int getNumTimesAnyBindingTypeEventObserved()
   {
      return numTimesAnyBindingTypeEventObserved;
   }
   
   public int getNumTimesNonRuntimeBindingTypeObserved()
   {
      return numTimesNonRuntimeBindingTypeObserved;
   }
   
   public void reset()
   {
      numTimesAnyBindingTypeEventObserved = 0;
      numTimesNonRuntimeBindingTypeObserved = 0;
   }
}
