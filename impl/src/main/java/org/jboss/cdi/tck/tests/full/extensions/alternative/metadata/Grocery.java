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
package org.jboss.cdi.tck.tests.full.extensions.alternative.metadata;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@SuppressWarnings("unused")
@ApplicationScoped
@Expensive
public class Grocery implements Shop {
    private Vegetables vegetables;
    @Inject
    private Fruit fruit;
    private boolean constructorWithParameterUsed = false;
    private static boolean disposerMethodCalled = false;
    private Fruit initializerFruit = null;
    private Fruit observerFruit = null;
    private Fruit disposerFruit = null;
    private Bread bread = new Bread(true);
    private Water water = null;
    private Vegetables wrappedEventParameter = null;
    private Vegetables wrappedDisposalParameter = null;

    private Milk observerEvent = null;
    private TropicalFruit observerParameter = null;
    private boolean observer2Used = false;

    public Grocery() {
    }

    public Grocery(@Any TropicalFruit fruit) {
        constructorWithParameterUsed = true;
    }

    public void nonInjectAnnotatedInitializer(@Any Water water) {
        this.water = water;
    }

    @Inject
    public void initializer(@Any Fruit fruit) {
        this.initializerFruit = fruit;
    }

    public String foo() {
        return "bar";
    }

    public boolean isVegetablesInjected() {
        return vegetables != null;
    }

    public Fruit getFruit() {
        return fruit;
    }

    public boolean isConstructorWithParameterUsed() {
        return constructorWithParameterUsed;
    }

    public Fruit getInitializerFruit() {
        return initializerFruit;
    }

    public Milk getMilk() {
        return new Milk(true);
    }

    public void observer1(Milk event, TropicalFruit fruit) {
        observerEvent = event;
        observerParameter = fruit;
    }

    public void observer2(@Observes Bread event) {
        observer2Used = true;
    }

    public void observerMilk(@Observes Milk milk, @Any Fruit fruit) {
        this.observerFruit = fruit;
    }

    public void observesVegetable(@Observes Vegetables vegetable) {
        wrappedEventParameter = vegetable;
    }

    @Produces
    @Cheap
    public Yogurt getYogurt(@Any TropicalFruit fruit) {
        return new Yogurt(fruit);
    }

    @Produces
    @Cheap
    public Bill createBill(@Any Fruit fruit) {
        return new Bill(fruit);
    }

    @Produces
    @Cheap
    public Vegetables createVegetable() {
        return new Carrot();
    }

    public void destroyBill(@Disposes @Cheap Bill bill, Fruit fruit) {
        disposerFruit = fruit;
    }

    public void destroyVegetable(@Disposes Vegetables vegetables) {
        wrappedDisposalParameter = vegetables;
    }

    public void destroyYogurt(Yogurt yogurt){
       disposerMethodCalled = true;
    };

    public boolean isWaterInjected() {
        return water != null;
    }

    public Milk getObserverEvent() {
        return observerEvent;
    }

    public TropicalFruit getObserverParameter() {
        return observerParameter;
    }

    public boolean isObserver2Used() {
        return observer2Used;
    }

    public Fruit getObserverFruit() {
        return observerFruit;
    }

    public Fruit getDisposerFruit() {
        return disposerFruit;
    }

    public Vegetables getWrappedEventParameter() {
        return wrappedEventParameter;
    }

    public Vegetables getWrappedDisposalParameter() {
        return wrappedDisposalParameter;
    }

    public static boolean isDisposerMethodCalled() {
        return disposerMethodCalled;
    }

}
