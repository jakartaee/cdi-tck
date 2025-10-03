/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.alternative.broken.reserve;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Reserve;

@Alternative
@Priority(1)
@Dependent
public class AlternativeWithReserveProducer {
    @Reserve
    @Priority(1)
    @Produces
    public String produce() {
        return "wrong";
    }
}
