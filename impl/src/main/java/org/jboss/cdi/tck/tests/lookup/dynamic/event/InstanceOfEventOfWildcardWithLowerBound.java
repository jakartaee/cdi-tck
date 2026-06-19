/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.dynamic.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class InstanceOfEventOfWildcardWithLowerBound {
    static List<String> seen = new ArrayList<>();

    @Inject
    Instance<Event<? super Collection<String>>> instance;

    public void fire() {
        instance.get().fire(List.of("hello", "world"));
        instance.get().fire(List.of("hi", "there"));
    }

    public void observe(@Observes List<String> str) {
        seen.addAll(str);
    }
}
