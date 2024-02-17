/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Dependent
public class DuckNotifier {

    @Inject
    DuckObserver observer;

    @Inject
    BeanManager beanManager;

    @Inject
    @Any
    Event<Duck<String>> event;

    @Inject
    @Bravo
    Event<Duck<Map<String, Integer>>> mapDuckEvent;

    @Inject
    @Any
    Event<List<Duck<Number>>> listDuckEvent;

    public void fireStringDuck() {
        observer.reset();
        event.fire(new Duck<String>());
    }

    public void fireMapDuck() {
        observer.reset();
        mapDuckEvent.fire(new Duck<Map<String,Integer>>());
    }


    public void fireListDuck() {
        observer.reset();
        listDuckEvent.fire(new ArrayList<Duck<Number>>());
    }



}
