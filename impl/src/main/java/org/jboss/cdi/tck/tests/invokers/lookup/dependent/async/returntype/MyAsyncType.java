/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.returntype;

import java.util.concurrent.CompletableFuture;

public interface MyAsyncType<T> {
    boolean isComplete();

    T getIfComplete();

    MyAsyncType<T> whenComplete(Runnable callback);

    static <T> MyAsyncType<T> from(CompletableFuture<T> future) {
        return new MyAsyncTypeImpl<>(future);
    }
}
