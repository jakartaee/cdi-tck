/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.paramtype;

import java.util.concurrent.atomic.AtomicReference;

final class MyAsyncTypeImpl<T> implements MyAsyncType<T> {
    private final AtomicReference<T> value = new AtomicReference<>(null);
    private final AtomicReference<Runnable> callback = new AtomicReference<>(null);

    MyAsyncTypeImpl() {
    }

    @Override
    public boolean isComplete() {
        return value.get() != null;
    }

    @Override
    public T getIfComplete() {
        T value = this.value.get();
        if (value != null) {
            return value;
        } else {
            throw new IllegalStateException("not yet complete");
        }
    }

    @Override
    public void whenComplete(Runnable callback) {
        if (!this.callback.compareAndSet(null, callback)) {
            throw new IllegalStateException("only one callback possible");
        }
    }

    @Override
    public void resume(T value) {
        if (value == null) {
            throw new IllegalArgumentException("must resume with non-null value");
        }
        if (this.value.compareAndSet(null, value)) {
            Runnable callback = this.callback.get();
            if (callback != null) {
                callback.run();
            }
        }
    }
}
