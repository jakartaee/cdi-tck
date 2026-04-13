/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.paramtype.multiple;

import java.util.concurrent.CompletableFuture;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyBean {
    static boolean futureComplete = false;

    public void hello(MyDependentBean bean, CompletableFuture<String> future, MyAsyncType async1, MyAsyncType async2) {
        future.whenComplete((value, error) -> {
            futureComplete = true;
        });
    }
}
