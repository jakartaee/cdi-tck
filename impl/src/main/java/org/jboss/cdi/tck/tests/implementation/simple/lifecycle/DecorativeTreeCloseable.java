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

public class DecorativeTreeCloseable implements AutoCloseable {
    private static boolean closeCalled;

    private final TreeBranch branch;

    public DecorativeTreeCloseable(TreeBranch branch) {
        closeCalled = false;
        this.branch = branch;
    }

    @Override
    public void close() throws Exception {
        assert !TreeBranch.isBeanDestroyed();
        closeCalled = true;
    }

    public static boolean isCloseCalled() {
        return closeCalled;
    }
}
