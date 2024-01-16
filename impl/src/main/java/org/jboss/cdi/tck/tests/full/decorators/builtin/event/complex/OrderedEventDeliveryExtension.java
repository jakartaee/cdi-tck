/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.decorators.builtin.event.complex;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ObserverMethod;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;

public class OrderedEventDeliveryExtension implements Extension {

    private final Map<ObserverMethod<?>, Integer> observerMethodOrder = new ConcurrentHashMap<ObserverMethod<?>, Integer>();

    void processObserverMethod(@Observes ProcessObserverMethod<?, ?> event) {
        ObserverMethod<?> observer = event.getObserverMethod();
        AnnotatedMethod<?> method = event.getAnnotatedMethod();

        int order = 0; // the default order is 0
        if (method.isAnnotationPresent(Ordered.class)) {
            Ordered ordered = method.getAnnotation(Ordered.class);
            order = ordered.value();
        }
        observerMethodOrder.put(observer, order);
    }

    public int getOrder(ObserverMethod<?> observer) {
        Integer order = observerMethodOrder.get(observer);
        if (order == null) {
            return 0;
        }
        return order.intValue();
    }
}
