/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
// reserve and priority gained via stereotype
@ReservePriorityStereotype
public class Qux {
    public String ping() {
        return Qux.class.getSimpleName();
    }
}
