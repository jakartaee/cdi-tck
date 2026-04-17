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

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

@Dependent
public class Venom {
    private static boolean destroyed;

    public Venom() {
        destroyed = false;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }
}
