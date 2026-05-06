/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.autoclose.synthetic.bean.dependent;

public class DependentBean implements AutoCloseable {
    public static boolean closed = false;

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
