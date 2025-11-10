/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.eager.producer.method;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.Eager;
import jakarta.enterprise.inject.Produces;

@Dependent
public class ProducerMethods {
    @ApplicationScoped
    @Eager
    @Produces
    public EagerClass produceEager() {
        return new EagerClass(0);
    }

    @ApplicationScoped
    @Produces
    public LazyClass produceLazy() {
        return new LazyClass(0);
    }

    @ApplicationScoped
    @AQualifier
    @Eager
    @Produces
    public QualifiedEagerClass produceQualifiedEager() {
        return new QualifiedEagerClass(0);
    }

    @ApplicationScoped
    @AQualifier
    @Produces
    public QualifiedLazyClass produceQualifiedLazy() {
        return new QualifiedLazyClass(0);
    }
}
