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
import jakarta.enterprise.context.Dependent;

@Dependent
public class TreeBranch {
    private static boolean beanDestroyed = false;

    public TreeBranch() {
        beanDestroyed = false;
    }

    @PreDestroy
    public void destroy() {
        beanDestroyed = true;
    }

    public static boolean isBeanDestroyed() {
        return beanDestroyed;
    }
}
