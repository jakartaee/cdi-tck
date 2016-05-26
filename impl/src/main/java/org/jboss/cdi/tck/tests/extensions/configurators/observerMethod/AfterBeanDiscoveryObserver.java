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

import org.jboss.cdi.tck.literals.ObservesLiteral;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public class AfterBeanDiscoveryObserver implements Extension {

    private ObserverMethod<Banana> bananaObserverMethod;

    public static AtomicBoolean newPineappleObserverNotified = new AtomicBoolean(false);
    public static AtomicBoolean newMelonObserverNotified = new AtomicBoolean(false);
    public static AtomicBoolean newPeachObserverNotified = new AtomicBoolean(false);

    AnnotatedMethod<? super FruitObserver> peachObserver;

    void processAnnotatedType(@Observes ProcessAnnotatedType<FruitObserver> pat) {

        peachObserver = pat.getAnnotatedType().getMethods().stream()
                .filter(annotatedMethod -> annotatedMethod.getJavaMember().getName().equals("observesPeach")).findAny().get();

        // add @Observes to the method param
        pat.configureAnnotatedType().filterMethods(annotatedMethod -> annotatedMethod.equals(peachObserver)).findAny().get().params().stream().findAny().get()
                .add(ObservesLiteral.INSTANCE);
    }

    void processObserverMethod(@Observes ProcessObserverMethod<Banana, FruitObserver> event) {
        bananaObserverMethod = event.getObserverMethod();
    }

    void observesABD(@Observes AfterBeanDiscovery abd) throws NoSuchMethodException {

        // read from ObserverMethod
        abd.<Banana> addObserverMethod().read(bananaObserverMethod).beanClass(FruitObserver.class).observedType(Pineapple.class).addQualifier(Delicious.DeliciousLiteral.INSTANCE)
                .notifyWith((b) -> {
                    newPineappleObserverNotified.set(true);
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
    }

    public static void reset() {
        newMelonObserverNotified.set(false);
        newPineappleObserverNotified.set(false);
        newPeachObserverNotified.set(false);
    }

}
