/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Dependent
public class Clients {
    @Inject
    Provider<Animal> provider1;

    @Inject
    Provider<? extends Animal> provider2;

    @Inject
    @Whitefish
    Provider<Animal> provider3;

    @Inject
    @Whitefish
    Provider<? extends Animal> provider4;

    @Inject
    Instance<Animal> instance1;

    @Inject
    Instance<? extends Animal> instance2;

    @Inject
    @Whitefish
    Instance<Animal> instance3;

    @Inject
    @Whitefish
    Instance<? extends Animal> instance4;

    // ---

    @Inject
    Provider<Cat<European>> parameterizedProvider1;

    @Inject
    Provider<? extends Cat<European>> parameterizedProvider2;

    @Inject
    @Whitefish
    Provider<Cat<European>> parameterizedProvider3;

    @Inject
    @Whitefish
    Provider<? extends Cat<European>> parameterizedProvider4;

    @Inject
    Instance<Cat<European>> parameterizedInstance1;

    @Inject
    Instance<? extends Cat<European>> parameterizedInstance2;

    @Inject
    @Whitefish
    Instance<Cat<European>> parameterizedInstance3;

    @Inject
    @Whitefish
    Instance<? extends Cat<European>> parameterizedInstance4;

    // ---

    @Inject
    Provider<Cat> rawProvider1;

    @Inject
    Provider<? extends Cat> rawProvider2;

    @Inject
    @Whitefish
    Provider<Cat> rawProvider3;

    @Inject
    @Whitefish
    Provider<? extends Cat> rawProvider4;

    @Inject
    Instance<Cat> rawInstance1;

    @Inject
    Instance<? extends Cat> rawInstance2;

    @Inject
    @Whitefish
    Instance<Cat> rawInstance3;

    @Inject
    @Whitefish
    Instance<? extends Cat> rawInstance4;

    // ---

    @Inject
    Provider<Spider[]> arrayProvider1;

    @Inject
    Provider<? extends Spider[]> arrayProvider2;

    @Inject
    @Whitefish
    Provider<Spider[]> arrayProvider3;

    @Inject
    @Whitefish
    Provider<? extends Spider[]> arrayProvider4;

    @Inject
    Instance<Spider[]> arrayInstance1;

    @Inject
    Instance<? extends Spider[]> arrayInstance2;

    @Inject
    @Whitefish
    Instance<Spider[]> arrayInstance3;

    @Inject
    @Whitefish
    Instance<? extends Spider[]> arrayInstance4;

    // ---

    @Inject
    Event<Animal> event1;

    @Inject
    Event<? super Animal> event2;

    @Inject
    @Whitefish
    Event<Animal> event3;

    @Inject
    @Whitefish
    Event<? super Animal> event4;

    // ---

    @Inject
    Event<Cat<African>> parameterizedEvent1;

    @Inject
    Event<? super Cat<African>> parameterizedEvent2;

    @Inject
    @Whitefish
    Event<Cat<African>> parameterizedEvent3;

    @Inject
    @Whitefish
    Event<? super Cat<African>> parameterizedEvent4;

    // ---

    @Inject
    Event<Cat> rawEvent1;

    @Inject
    Event<? super Cat> rawEvent2;

    @Inject
    @Whitefish
    Event<Cat> rawEvent3;

    @Inject
    @Whitefish
    Event<? super Cat> rawEvent4;

    // ---

    @Inject
    Event<Spider[]> arrayEvent1;

    @Inject
    Event<? super Spider[]> arrayEvent2;

    @Inject
    @Whitefish
    Event<Spider[]> arrayEvent3;

    @Inject
    @Whitefish
    Event<? super Spider[]> arrayEvent4;
}
