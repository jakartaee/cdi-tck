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
package org.jboss.cdi.tck.tests.full.extensions.configurators.observerMethod;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Reception;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ObserverMethod;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;

public class ProcessObserverMethodObserver implements Extension {

    public static AtomicBoolean consumerNotified = new AtomicBoolean(false);
    public static Set<Annotation> pineAppleQualifiers;
    private ObserverMethod<Kiwi> originalOM;

    void observesPearPOM(@Observes ProcessObserverMethod<Pear, FruitObserver> event) {
        // add @Ripe and @Delicious to the observed type
        // make observer asynchronous
        // set priority
        event.configureObserverMethod()
                .addQualifiers(Ripe.RipeLiteral.INSTANCE, Delicious.DeliciousLiteral.INSTANCE)
                .async(true)
                .priority(ObserverMethod.DEFAULT_PRIORITY + 100);
    }

    void observesOrangePOM(@Observes ProcessObserverMethod<Orange, FruitObserver> event) {
        // replace qualifiers
        // set reception to Reception.IF_EXISTS
        // set transaction phase to TransactionPhase.AFTER_SUCCESS
        event.configureObserverMethod()
                .qualifiers(Delicious.DeliciousLiteral.INSTANCE)
                .reception(Reception.IF_EXISTS)
                .transactionPhase(TransactionPhase.AFTER_SUCCESS);
    }

    void observesPineapplePOM(@Observes ProcessObserverMethod<Pineapple, FruitObserver> event) {
        event.configureObserverMethod()
                .addQualifier(Delicious.DeliciousLiteral.INSTANCE)
                .notifyWith((eventConsumer) -> {
                    pineAppleQualifiers = eventConsumer.getMetadata().getQualifiers();
                    consumerNotified.set(true);
                });
    }

    void observesKiwiPOM(@Observes ProcessObserverMethod<Kiwi, FruitObserver> event) {
        originalOM = event.getObserverMethod();
        event.configureObserverMethod().reception(Reception.IF_EXISTS);
    }

    public ObserverMethod<Kiwi> getOriginalOM() {
        return originalOM;
    }
}
