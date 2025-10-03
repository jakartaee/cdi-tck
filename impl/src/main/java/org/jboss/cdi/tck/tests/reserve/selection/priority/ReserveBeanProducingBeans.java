/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection.priority;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Reserve;

@Reserve
@Priority(1)
@ApplicationScoped
public class ReserveBeanProducingBeans {
    @ProducedByMethod
    @Produces
    Delta producer1() {
        return new Delta(ReservePriorityTest.DEFAULT);
    }

    @ProducedByField
    @Produces
    Delta producer2 = new Delta(ReservePriorityTest.DEFAULT);
}
