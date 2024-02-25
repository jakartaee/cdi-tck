/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.annotatedTypeConfigurator;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

// all annotations removed during PAT
@Named
@ApplicationScoped
public class AnimalShelter {

    private boolean isPostConstructCalled = false;
    private boolean isRoomObserved = false;

    @Inject
    Cat cat;

    @Produces
    Dog dog = new Dog("jack");

    public AnimalShelter() {
    }

    @Inject
    public AnimalShelter(Cat cat, Dog dog) {

    }

    @AroundInvoke
    public Object test(InvocationContext ctx) {
        return null;
    }

    @PostConstruct
    public void postConstruct() {
        isPostConstructCalled = true;
    }

    public boolean isPostConstructCalled() {
        return isPostConstructCalled;
    }

    public boolean isRoomObserved() {
        return isRoomObserved;
    }

    public void observesRoomInShelter(@Observes @Cats Room room) {
        isRoomObserved = true;
    }

    public Cat getCat() {
        return cat;
    }
}
