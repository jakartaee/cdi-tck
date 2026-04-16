/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsDependent;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

@Dependent
public class TrackedBean {
    static final AtomicInteger createdCount = new AtomicInteger(0);
    static final AtomicInteger destroyedCount = new AtomicInteger(0);

    static void reset() {
        createdCount.set(0);
        destroyedCount.set(0);
    }

    @PostConstruct
    void postConstruct() {
        createdCount.incrementAndGet();
    }

    @PreDestroy
    void preDestroy() {
        destroyedCount.incrementAndGet();
    }

    public String ping() {
        return "tracked";
    }
}
