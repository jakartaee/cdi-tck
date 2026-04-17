/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.implementation.producer.method.lifecycle;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JumpingSpiderProducer {
    private static boolean disposerCalled = false;

    @Produces
    @Dependent
    @AutoClose
    JumpingSpider produce(Venom venom) {
        return new JumpingSpider(venom);
    }

    void dispose(@Disposes JumpingSpider spider) {
        assert !disposerCalled;
        assert !Venom.isDestroyed();
        disposerCalled = true;
    }

    public static boolean isDisposerCalled() {
        return disposerCalled;
    }
}
