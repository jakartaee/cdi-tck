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
package org.jboss.jsr299.tck.tests.event.eventTypes;

import javax.enterprise.event.Observes;

/**
 * This class declares observer methods for every superclass and interface
 * contained in the hierarchy for the {@link ComplexEvent} event type.
 * 
 * @author David Allen
 */
public class EventTypeFamilyObserver
{
   private static int objectEventQuantity = 0;
   private static int generalEventQuantity = 0;
   private static int abstractEventQuantity = 0;
   private static int complexEventQuantity = 0;

   public void observeObject(@Observes Object event)
   {
      // Avoid counting implicit container events
      if (event instanceof ComplexEvent)
      {
         objectEventQuantity++;
      }
   }

   public void observeGeneralEvent(@Observes GeneralEvent event)
   {
      generalEventQuantity++;
   }

   public void observeAbstractEvent(@Observes AbstractEvent event)
   {
      abstractEventQuantity++;
   }

   public void observeComplexEvent(@Observes ComplexEvent event)
   {
      complexEventQuantity++;
   }

   public int getGeneralEventQuantity()
   {
      return generalEventQuantity;
   }
   
   public int getAbstractEventQuantity()
   {
      return abstractEventQuantity;
   }
   
   public int getComplexEventQuantity()
   {
      return complexEventQuantity;
   }
   
   public int getObjectEventQuantity()
   {
      return objectEventQuantity;
   }
   
   public int getTotalEventsObserved()
   {
      return objectEventQuantity + generalEventQuantity + abstractEventQuantity + complexEventQuantity;
   }
   
   public void reset()
   {
      objectEventQuantity = 0;
      generalEventQuantity = 0;
      abstractEventQuantity = 0;
      complexEventQuantity = 0;
   }
}
