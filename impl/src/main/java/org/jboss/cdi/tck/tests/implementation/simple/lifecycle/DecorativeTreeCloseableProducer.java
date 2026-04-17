/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.implementation.simple.lifecycle;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class DecorativeTreeCloseableProducer {
    private static boolean disposerCalled = false;

    public DecorativeTreeCloseableProducer() {
        disposerCalled = false;
    }

    @Produces
    @Dependent
    @AutoClose
    DecorativeTreeCloseable produce(TreeBranch branch) {
        return new DecorativeTreeCloseable(branch);
    }

    void dispose(@Disposes DecorativeTreeCloseable decorativeTree) {
        assert !DecorativeTreeCloseable.isCloseCalled();
        assert !TreeBranch.isBeanDestroyed();
        disposerCalled = true;
    }

    public static boolean isDisposerCalled() {
        return disposerCalled;
    }
}
