/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjections;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanDisposer;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticInjections;

public class SyntheticPojoDisposer implements SyntheticBeanDisposer<SyntheticPojo> {
    static final AtomicBoolean disposed = new AtomicBoolean(false);
    static String bravoValueInDisposer = null;

    @Override
    public void dispose(SyntheticPojo instance, SyntheticInjections injections, Parameters params) {
        Bravo bravo = injections.get(Bravo.class);
        bravoValueInDisposer = bravo.value();
        disposed.set(true);
    }
}
