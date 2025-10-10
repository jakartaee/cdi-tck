/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Reserve;

@Dependent
public class BarProducer {
    @Produces
    @Reserve
    @Priority(1100)
    @Wild
    public final Bar producedBar = new Bar();

    @Produces
    @Reserve
    @Priority(1100)
    @Tame
    public Bar produceTameBar() {
        return new Bar();
    }
}
