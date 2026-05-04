/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.full.invokers.async.returntype;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

@Dependent
public class MyDependentBean {
    public static AtomicInteger destroyedCounter = new AtomicInteger(0);

    public static void reset() {
        destroyedCounter.set(0);
    }

    @PreDestroy
    public void destroy() {
        destroyedCounter.incrementAndGet();
    }
}
