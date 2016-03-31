/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.observerMethodConfigurator;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.interceptor.Interceptor;

public class ProcessObserverMethodObserver implements Extension {

    public static AtomicBoolean consumerNotified = new AtomicBoolean(false);
    public static Set<Annotation> pineAppleQualifiers;

    void observesBreadPOM(ProcessObserverMethod<Bread, FoodObserver> event) {
        // set beanClass of the observer to FruitObserver
        // set observed typ to Apple
        event.configureObserverMethod().read(event.getAnnotatedMethod())
                .beanClass(FruitObserver.class)
                .observedType(Apple.class);

    }

    void observesPearPOM(ProcessObserverMethod<Pear, FruitObserver> event) {
        // add @Ripe and @Delicious to the observed type
        // make observer asynchronous
        // set priority
        event.configureObserverMethod().read(event.getObserverMethod())
                .addQualifiers(Ripe.RipeLiteral.INSTANCE, Delicious.DeliciousLiteral.INSTANCE)
                .async(true)
                .priority(Interceptor.Priority.APPLICATION + 100);
    }

    void observesOrangePOM(ProcessObserverMethod<Orange, FruitObserver> event) {
        // replace qualifiers
        // set reception to Reception.IF_EXISTS
        // set transaction phase to TransactionPhase.AFTER_SUCCESS
        event.configureObserverMethod().read(event.getAnnotatedMethod().getJavaMember())
                .qualifiers(Delicious.DeliciousLiteral.INSTANCE)
                .reception(Reception.IF_EXISTS)
                .transactionPhase(TransactionPhase.AFTER_SUCCESS);
    }

    void observesPineapplePOM(ProcessObserverMethod<Pineapple, FruitObserver> event) {
        event.configureObserverMethod().read(event.getObserverMethod()).addQualifier(Delicious.DeliciousLiteral.INSTANCE)
                .notifyWith((pineapple, eventMetadata) -> {
                    pineAppleQualifiers = eventMetadata.getQualifiers();
                    consumerNotified.set(true);
                });
    }
}
