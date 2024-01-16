/*
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
package org.jboss.cdi.tck.tests.event.bindingTypes;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

@Dependent
public class AnimalAssessment {
    @Inject
    @Any
    Event<Animal> animalEvent;

    @Inject
    @Tame
    Event<Animal> tameAnimalEvent;

    @Inject
    @Any
    @Wild
    Event<Animal> wildAnimalEvent;

    public void classifyAsTame(Animal animal) {
        tameAnimalEvent.fire(animal);
    }

    public void classifyAsWild(Animal animal) {
        wildAnimalEvent.fire(animal);
    }

    public void assess(Animal animal) {
        animalEvent.fire(animal);
    }
}
