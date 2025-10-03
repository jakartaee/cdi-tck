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

@ApplicationScoped
public class BeanProducingReserveBeans {
    @Reserve
    @Priority(10)
    @ProducedByMethod
    @Produces
    Alpha producer1() {
        return new Alpha(ReservePriorityTest.RESERVE);
    }

    @Reserve
    @Priority(10)
    @ProducedByField
    @Produces
    Alpha producer2 = new Alpha(ReservePriorityTest.RESERVE);

    @Reserve
    @Priority(10)
    @ProducedByMethod
    @Produces
    Beta producer3() {
        return new Beta(ReservePriorityTest.RESERVE);
    }

    @Reserve
    @Priority(10)
    @ProducedByField
    @Produces
    Beta producer4 = new Beta(ReservePriorityTest.RESERVE);

    @Reserve
    @Priority(10)
    @ProducedByMethod
    @Produces
    Gamma producer5() {
        return new Gamma(ReservePriorityTest.RESERVE);
    }

    @Reserve
    @Priority(10)
    @ProducedByField
    @Produces
    Gamma producer6 = new Gamma(ReservePriorityTest.RESERVE);

    @Reserve
    @Priority(10)
    @ProducedByMethod
    @Produces
    Delta producer7() {
        return new Delta(ReservePriorityTest.RESERVE);
    }

    @Reserve
    @Priority(10)
    @ProducedByField
    @Produces
    Delta producer8 = new Delta(ReservePriorityTest.RESERVE);
}
