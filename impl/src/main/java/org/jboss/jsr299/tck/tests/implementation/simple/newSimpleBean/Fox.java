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
package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

@AnimalStereotype
class Fox
{
   
   @Produces
   private Den den = new Den("FoxDen");
   
   private int nextLitterSize;
   
   private boolean litterDisposed = false;
   
   public void observeEvent(@Observes String event)
   {
      
   }

   public Den getDen()
   {
      return den;
   }

   public void setDen(Den den)
   {
      this.den = den;
   }

   public int getNextLitterSize()
   {
      return nextLitterSize;
   }

   public void setNextLitterSize(int nextLitterSize)
   {
      this.nextLitterSize = nextLitterSize;
   }
   
   @Produces
   public Litter produceLitter()
   {
      return new Litter(nextLitterSize);
   }
   
   public void disposeLitter(@Disposes Litter litter)
   {
      this.litterDisposed = true;
   }

   public boolean isLitterDisposed()
   {
      return litterDisposed;
   }
}
