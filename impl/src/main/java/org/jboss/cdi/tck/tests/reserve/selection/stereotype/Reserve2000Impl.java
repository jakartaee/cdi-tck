/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection.stereotype;

import jakarta.annotation.Priority;

@ReserveStereotype
@Priority(2000)
public class Reserve2000Impl implements SomeInterface {
    @Override
    public String ping() {
        return Reserve2000Impl.class.getSimpleName();
    }
}
