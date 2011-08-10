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
package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@SuppressWarnings( "unused" )
@ApplicationScoped
@Expensive
public class Grocery implements Shop
{
   private Vegetables vegetables;
   @Inject
   private Fruit fruit;
   private boolean constructorWithParameterUsed = false;
   private TropicalFruit initializerFruit = null;
   private Bread bread = new Bread(true);
   private Water water = null;
   
   private Milk observerEvent = null;
   private TropicalFruit observerParameter = null;
   private boolean observer2Used = false;
   
   public Grocery()
   {
   }
   
   public Grocery(@Any TropicalFruit fruit) {
      constructorWithParameterUsed = true;
   }

   public void nonInjectAnnotatedInitializer(@Any Water water) {
      this.water = water;
   }
   
   @Inject
   public void initializer(@Any TropicalFruit fruit) {
      this.initializerFruit = fruit;
   }
   
   public String foo()
   {
      return "bar";
   }

   public boolean isVegetablesInjected()
   {
      return vegetables != null;
   }

   public Fruit getFruit()
   {
      return fruit;
   }
   
   public boolean isConstructorWithParameterUsed()
   {
      return constructorWithParameterUsed;
   }

   public TropicalFruit getInitializerFruit()
   {
      return initializerFruit;
   }

   public Milk getMilk()
   {
      return new Milk(true);
   }

   @Produces @Cheap
   public Yogurt getYogurt(@Any TropicalFruit fruit)
   {
      return new Yogurt(fruit);
   }
   
   public void observer1(Milk event, TropicalFruit fruit) {
      observerEvent = event;
      observerParameter = fruit;
   }
   
   public void observer2(@Observes Bread event) {
      observer2Used = true;
   }
   
   public boolean isWaterInjected() {
      return water != null;
   }

   public Milk getObserverEvent()
   {
      return observerEvent;
   }

   public TropicalFruit getObserverParameter()
   {
      return observerParameter;
   }

   public boolean isObserver2Used()
   {
      return observer2Used;
   }
}
