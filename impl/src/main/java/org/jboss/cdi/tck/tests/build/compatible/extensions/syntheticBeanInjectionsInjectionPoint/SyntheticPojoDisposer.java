/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsInjectionPoint;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticInjections;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class SyntheticPojoDisposer implements SyntheticBeanDisposer<SyntheticPojo> {
    static InjectionPoint lookedUp = null;

    @Override
    public void dispose(SyntheticPojo instance, SyntheticInjections injections, Parameters params) {
        try {
            lookedUp = injections.get(InjectionPoint.class);
        } catch (Exception ignored) {
            // The spec says looking up InjectionPoint in a disposer is "invalid".
            // The container may throw any exception.
        }
    }
}
