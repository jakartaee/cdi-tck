/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.builtin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyBean {
    public CompletionStage<String> helloCS(MyDependentBean bean, CompletableFuture<String> future) {
        return helloCF(bean, future);
    }

    public CompletableFuture<String> helloCF(MyDependentBean bean, CompletableFuture<String> future) {
        CompletableFuture<String> result = new CompletableFuture<>();
        future.whenComplete((value, error) -> {
            if (error == null) {
                result.complete(value);
            } else {
                result.completeExceptionally(error);
            }
        });
        return result;
    }

    public Flow.Publisher<String> helloFP(MyDependentBean bean, CompletableFuture<String> future) {
        return new CompletableFuturePublisher<>(future);
    }
}
