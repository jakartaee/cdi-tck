/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.paramtype.invalid;

import jakarta.enterprise.invoke.AsyncHandler;

public class AsyncHandlerArray implements AsyncHandler.ParameterType<String[]> {
    @Override
    public String[] transformArgument(String[] original, Runnable completion) {
        return original;
    }
}
