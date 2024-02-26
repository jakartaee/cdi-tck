/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

@DogBinding
@Dependent
class Dog extends Animal {
    protected static final String DOG = "Dog";

    private static int numberOfInterceptions = 0;

    @Override
    public String getAnimalType() {
        return DOG;
    }

    @PostConstruct
    @PreDestroy
    public void intercept() {
        numberOfInterceptions++;
    }

    public static int getNumberOfInterceptions() {
        return numberOfInterceptions;
    }

    public static void reset() {
        numberOfInterceptions = 0;
    }
}
