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

@AsyncHandler.ReturnType
@AsyncHandler.ParameterType
public class AsyncHandlerBothAnnotations<T> implements AsyncHandler<MyAsyncType<T>> {
    @Override
    public MyAsyncType<T> transform(MyAsyncType<T> original, Runnable completion) {
        return original;
    }
}
