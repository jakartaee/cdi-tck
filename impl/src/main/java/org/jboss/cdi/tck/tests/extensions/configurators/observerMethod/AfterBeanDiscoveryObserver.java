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
package org.jboss.cdi.tck.tests.extensions.configurators.observerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessObserverMethod;

public class AfterBeanDiscoveryObserver implements Extension {

    private ObserverMethod<Fruit> fruitObserverMethod;

    public static AtomicBoolean newBananaObserverNotified = new AtomicBoolean(false);
    public static AtomicBoolean newMelonObserverNotified = new AtomicBoolean(false);
    public static AtomicBoolean newPeachObserverNotified = new AtomicBoolean(false);
    public static AtomicBoolean newPapayaObserverNotified = new AtomicBoolean(false);

    AnnotatedMethod<? super FruitObserver> peachObserver;

    void processAnnotatedType(@Observes ProcessAnnotatedType<FruitObserver> pat) {

        peachObserver = pat.getAnnotatedType().getMethods().stream()
                .filter(annotatedMethod -> annotatedMethod.getJavaMember().getName().equals("observesPeach")).findAny().get();

    }

    void processObserverMethod(@Observes ProcessObserverMethod<Fruit, FruitObserver> event) {
        fruitObserverMethod = event.getObserverMethod();
    }

    void observesABD(@Observes AfterBeanDiscovery abd) throws NoSuchMethodException {

        // read from ObserverMethod
        abd.<Fruit>addObserverMethod().read(fruitObserverMethod).beanClass(FruitObserver.class).observedType(Banana.class)
                .addQualifiers(Ripe.RipeLiteral.INSTANCE, Any.Literal.INSTANCE)
                .notifyWith((b) -> {
                    newBananaObserverNotified.set(true);
                });

        // read from Method
        Method melonMethod = FruitObserver.class.getMethod("observesMelon", Melon.class);
        abd.addObserverMethod().read(melonMethod).observedType(Melon.class).notifyWith(m -> {
            newMelonObserverNotified.set(true);
        });

        // read from AnnotatedMethod
        abd.addObserverMethod().read(peachObserver).beanClass(FruitObserver.class).observedType(Peach.class).notifyWith((a) -> {
            newPeachObserverNotified.set(true);
        });

        abd.addObserverMethod(new ObserverMethod<Cherry>() {

            @Override
            public Class<?> getBeanClass() {
                return FruitObserver.class;
            }

            @Override
            public Type getObservedType() {
                return Cherry.class;
            }

            @Override
            public Set<Annotation> getObservedQualifiers() {
                return Collections.emptySet();
            }

            @Override
            public Reception getReception() {
                return Reception.ALWAYS;
            }

            @Override
            public TransactionPhase getTransactionPhase() {
                return TransactionPhase.IN_PROGRESS;
            }

            @Override
            public void notify(Cherry cherry) {

            }
        });

        abd.addObserverMethod().observedType(Papaya.class).reception(Reception.ALWAYS).notifyWith(eventContext -> newPapayaObserverNotified.set(true));
    }

    public static void reset() {
        newMelonObserverNotified.set(false);
        newBananaObserverNotified.set(false);
        newPeachObserverNotified.set(false);
    }

}
