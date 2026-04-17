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
package org.jboss.cdi.tck.tests.full.extensions.producer;

import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

public class DogProducer {
    public static final String QUIET_DOG_COLOR = "Black";
    public static final String NOISY_DOG_COLOR = "White";
    private static boolean noisyDogProducerCalled;
    private static boolean noisyDogDisposerCalled;
    private static boolean barkingDogDisposerCalled;
    private static boolean howlingDogDisposerCalled;

    @Produces
    @Quiet
    private Dog quietDog = new Dog(QUIET_DOG_COLOR);

    @Produces
    @Noisy
    public Dog produceNoisyDog(DogBed dogBed) {
        noisyDogProducerCalled = true;
        return new Dog(NOISY_DOG_COLOR);
    }

    @Produces
    @Barking
    @AutoClose
    public VocalDog produceBarkingDog = new BarkingDog();

    @Produces
    @Howling
    @AutoClose
    public VocalDog produceHowlingDog() {
        return new HowlingDog();
    }

    public void disposeNoisyDog(@Disposes @Noisy Dog dog) {
        noisyDogDisposerCalled = true;
    }

    public void disposeBarkingDog(@Disposes @Barking VocalDog dog) {
        barkingDogDisposerCalled = true;
    }

    public void disposeHowlingDog(@Disposes @Howling VocalDog dog) {
        assert !HowlingDog.closed;
        howlingDogDisposerCalled = true;
    }

    public static boolean isNoisyDogProducerCalled() {
        return noisyDogProducerCalled;
    }

    public static boolean isNoisyDogDisposerCalled() {
        return noisyDogDisposerCalled;
    }

    public static boolean isBarkingDogDisposerCalled() {
        return barkingDogDisposerCalled;
    }

    public static boolean isHowlingDogDisposerCalled() {
        return howlingDogDisposerCalled;
    }

    public static void reset() {
        DogProducer.noisyDogProducerCalled = false;
        DogProducer.noisyDogDisposerCalled = false;
        DogProducer.barkingDogDisposerCalled = false;
        DogProducer.howlingDogDisposerCalled = false;
    }
}
