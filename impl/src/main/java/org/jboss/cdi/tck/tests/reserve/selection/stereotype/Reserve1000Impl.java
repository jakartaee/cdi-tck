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

@ReserveStereotype
public class Reserve1000Impl implements SomeInterface {
    @Override
    public String ping() {
        return Reserve1000Impl.class.getSimpleName();
    }
}
