/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.autoclose.bean;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.context.Dependent;

@Dependent
@AutoClose
public class WithAutoCloseBean implements AutoCloseable {
    public boolean destroyed = false;
    public boolean closed = false;

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
