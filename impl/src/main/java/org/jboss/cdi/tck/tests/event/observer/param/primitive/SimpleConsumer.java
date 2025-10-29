/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.event.observer.param.primitive;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@Dependent
public class SimpleConsumer {
    static int intCounter = 0;
    static int longCounter = 0;

    public void observeInt(@Observes int value) {
        intCounter++;
    }

    public void observeLong(@Observes long value) {
        longCounter++;
    }
}
