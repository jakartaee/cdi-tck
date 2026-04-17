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

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
@AutoClose
public class FishPondCloseable implements AutoCloseable {
    private static boolean beanDestroyed;
    private static boolean beanClosed;

    @Inject
    public Salmon salmon;

    public FishPondCloseable() {
        beanDestroyed = false;
        beanClosed = false;
    }

    @PreDestroy
    public void destroy() {
        assert !beanDestroyed;
        assert !beanClosed;
        assert !Salmon.isBeanDestroyed();
        beanDestroyed = true;
    }

    @Override
    public void close() throws Exception {
        assert beanDestroyed;
        assert !beanClosed;
        assert !Salmon.isBeanDestroyed();
        beanClosed = true;
    }

    public static boolean isBeanDestroyed() {
        return beanDestroyed;
    }

    public static boolean isBeanClosed() {
        return beanClosed;
    }
}
