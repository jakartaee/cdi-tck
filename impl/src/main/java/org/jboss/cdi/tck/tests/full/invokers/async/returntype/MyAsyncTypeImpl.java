/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.full.invokers.async.returntype;

import java.util.concurrent.CompletableFuture;

final class MyAsyncTypeImpl<T> implements MyAsyncType<T> {
    private final CompletableFuture<T> future;

    MyAsyncTypeImpl(CompletableFuture<T> future) {
        this.future = future;
    }

    @Override
    public boolean isComplete() {
        return future.isDone();
    }

    @Override
    public T getIfComplete() {
        if (future.isDone()) {
            return future.getNow(null);
        } else {
            throw new IllegalStateException("not yet complete");
        }
    }

    @Override
    public MyAsyncType<T> whenComplete(Runnable callback) {
        CompletableFuture<T> newFuture = new CompletableFuture<>();
        future.whenComplete((value, error) -> {
            callback.run();

            if (error == null) {
                newFuture.complete(value);
            } else {
                newFuture.completeExceptionally(error);
            }
        });
        return new MyAsyncTypeImpl<>(newFuture);
    }
}
