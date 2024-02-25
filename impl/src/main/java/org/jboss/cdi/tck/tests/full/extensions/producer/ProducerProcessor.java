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

import java.util.Set;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeforeShutdown;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.InjectionTarget;
import jakarta.enterprise.inject.spi.ProcessInjectionTarget;
import jakarta.enterprise.inject.spi.ProcessProducer;
import jakarta.enterprise.inject.spi.Producer;

public class ProducerProcessor implements Extension {
    private static InjectionTarget<Cat> catInjectionTarget;
    private static Producer<Dog> noisyDogProducer;
    private static Producer<Dog> quietDogProducer;
    private static InjectionTarget<Dog> dogInjectionTarget;
    private static AnnotatedType<Dog> dogAnnotatedType;
    private static boolean overriddenCowProducerCalled;

    public void cleanup(@Observes BeforeShutdown shutdown) {
        catInjectionTarget = null;
        noisyDogProducer = null;
        quietDogProducer = null;
        dogInjectionTarget = null;
        dogAnnotatedType = null;
    }

    public void processDogProducerProducer(@Observes ProcessProducer<DogProducer, Dog> producerEvent) {
        if (producerEvent.getAnnotatedMember().isAnnotationPresent(Noisy.class)) {
            noisyDogProducer = producerEvent.getProducer();
            assert producerEvent.getAnnotatedMember() instanceof AnnotatedMethod<?>;
        } else if (producerEvent.getAnnotatedMember().isAnnotationPresent(Quiet.class)) {
            quietDogProducer = producerEvent.getProducer();
            assert producerEvent.getAnnotatedMember() instanceof AnnotatedField<?>;
        }
    }

    public void processCatProducer(@Observes ProcessInjectionTarget<Cat> event) {
        catInjectionTarget = event.getInjectionTarget();
    }

    public void processDogInjectionTarget(@Observes ProcessInjectionTarget<Dog> injectionTargetEvent) {
        // There a couple, but it does not matter which one is used for the tests
        dogInjectionTarget = injectionTargetEvent.getInjectionTarget();
        dogAnnotatedType = injectionTargetEvent.getAnnotatedType();
    }

    public void processCowProducer(@Observes ProcessProducer<CowProducer, Cow> event) {
        final Producer<Cow> producer = event.getProducer();
        event.setProducer(new Producer<Cow>() {

            public void dispose(Cow instance) {
                producer.dispose(instance);
            }

            public Set<InjectionPoint> getInjectionPoints() {
                return producer.getInjectionPoints();
            }

            public Cow produce(CreationalContext<Cow> ctx) {
                overriddenCowProducerCalled = true;
                return producer.produce(ctx);
            }
        });
    }

    public void processBirdCage(@Observes ProcessInjectionTarget<BirdCage> event) {
        event.setInjectionTarget(new CheckableInjectionTarget(event.getInjectionTarget()));
    }

    public static Producer<Dog> getNoisyDogProducer() {
        return noisyDogProducer;
    }

    public static Producer<Dog> getQuietDogProducer() {
        return quietDogProducer;
    }

    public static InjectionTarget<Cat> getCatInjectionTarget() {
        return catInjectionTarget;
    }

    public static InjectionTarget<Dog> getDogInjectionTarget() {
        return dogInjectionTarget;
    }

    public static AnnotatedType<Dog> getDogAnnotatedType() {
        return dogAnnotatedType;
    }

    public static void reset() {
        overriddenCowProducerCalled = false;
    }

    public static boolean isOverriddenCowProducerCalled() {
        return overriddenCowProducerCalled;
    }

}
