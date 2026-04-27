/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.returntype.invalid;

import jakarta.enterprise.invoke.AsyncHandler;

public class AsyncHandler2<T> implements AsyncHandler.ReturnType<MyAsyncType<T>> {
    @Override
    public MyAsyncType<T> transform(MyAsyncType<T> original, Runnable completion) {
        return original;
    }
}
