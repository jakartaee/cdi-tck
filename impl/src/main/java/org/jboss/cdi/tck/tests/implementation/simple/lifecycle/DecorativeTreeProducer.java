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
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class DecorativeTreeProducer {
    private static boolean disposerCalled = false;

    public DecorativeTreeProducer() {
        disposerCalled = false;
    }

    @Produces
    @Dependent
    DecorativeTree produce(TreeBranch branch) {
        return new DecorativeTree(branch);
    }

    void dispose(@Disposes DecorativeTree decorativeTree) {
        assert !TreeBranch.isBeanDestroyed();
        disposerCalled = true;
    }

    public static boolean isDisposerCalled() {
        return disposerCalled;
    }
}
