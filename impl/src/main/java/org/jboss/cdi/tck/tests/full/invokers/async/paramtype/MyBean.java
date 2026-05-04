/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.full.invokers.async.paramtype;

import static org.testng.Assert.assertNull;

import java.util.concurrent.CompletableFuture;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyBean {
    public void hello(MyDependentBean bean, CompletableFuture<String> future, MyAsyncType<String> async) {
        future.whenComplete((value, error) -> {
            assertNull(error);
            async.resume(value);
        });
    }

    public void helloThrow(MyDependentBean bean, CompletableFuture<String> future, MyAsyncType<String> async) {
        future.whenComplete((value, error) -> {
            assertNull(error);
            async.resume(value);
        });
        throw new IllegalArgumentException("synchronous throw");
    }
}
