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
@Priority(1000)
@ApplicationScoped
public class ReserveBeanProducingReserveBeans {
    @Reserve
    @Priority(20)
    @ProducedByMethod
    @Produces
    Beta producer1() {
        return new Beta(ReservePriorityTest.RESERVE2);
    }

    @Reserve
    @Priority(20)
    @ProducedByField
    @Produces
    Beta producer2 = new Beta(ReservePriorityTest.RESERVE2);

    // not enabled, because reserve producers do not inherit priority from declaring bean
    @Reserve
    @ProducedByMethod
    @Produces
    Gamma producer3() {
        return new Gamma(ReservePriorityTest.RESERVE2);
    }

    // not enabled, because reserve producers do not inherit priority from declaring bean
    @Reserve
    @ProducedByField
    @Produces
    Gamma producer4 = new Gamma(ReservePriorityTest.RESERVE2);
}
