/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.interceptors.invocation;

import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.context.Dependent;

@AlmightyBinding
@AutoClose
@Dependent
public class MissileCloseable implements AutoCloseable {
    boolean closed;

    public void reset() {
        closed = false;
    }

    @Override
    public void close() {
        closed = true;
    }
}
