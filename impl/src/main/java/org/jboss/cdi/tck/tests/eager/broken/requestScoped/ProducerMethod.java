/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.eager.broken.requestScoped;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.Eager;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;

@Dependent
public class ProducerMethod {
    @RequestScoped
    @Eager
    @Produces
    public EagerClass produceEager() {
        return new EagerClass();
    }
}
