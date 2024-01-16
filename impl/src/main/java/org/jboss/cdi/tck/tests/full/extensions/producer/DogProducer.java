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
package org.jboss.cdi.tck.tests.full.extensions.producer;

import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

public class DogProducer {
    public static final String QUIET_DOG_COLOR = "Black";
    public static final String NOISY_DOG_COLOR = "White";
    private static boolean noisyDogProducerCalled;
    private static boolean noisyDogDisposerCalled;

    @Produces
    @Quiet
    private Dog quietDog = new Dog(QUIET_DOG_COLOR);

    @Produces
    @Noisy
    public Dog produceNoisyDog(DogBed dogBed) {
        noisyDogProducerCalled = true;
        return new Dog(NOISY_DOG_COLOR);
    }

    public void disposeNoisyDog(@Disposes @Noisy Dog dog) {
        noisyDogDisposerCalled = true;
    }

    public static boolean isNoisyDogProducerCalled() {
        return noisyDogProducerCalled;
    }

    public static boolean isNoisyDogDisposerCalled() {
        return noisyDogDisposerCalled;
    }

    public static void reset() {
        DogProducer.noisyDogProducerCalled = false;
        DogProducer.noisyDogDisposerCalled = false;
    }
}
