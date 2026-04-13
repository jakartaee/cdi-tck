/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.invalid;

import jakarta.enterprise.invoke.AsyncHandler;

@AsyncHandler.ReturnType // to prevent a different deployment problem
public class AsyncHandlerRaw implements AsyncHandler {
    @Override
    public Object transform(Object original, Runnable completion) {
        return original;
    }
}
