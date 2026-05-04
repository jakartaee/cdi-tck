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

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyBean {
    public MyAsyncType<String> hello(MyDependentBean bean, CompletableFuture<String> future) {
        return MyAsyncType.from(future);
    }

    public MyAsyncType<String> helloThrow(MyDependentBean bean, CompletableFuture<String> future) {
        MyAsyncType<String> ignored = MyAsyncType.from(future);
        throw new IllegalArgumentException("synchronous throw");
    }
}
