/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.eager.synthetic.bean;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticInjections;

public class LazyClassCreator implements SyntheticBeanCreator<LazyClass> {
    static boolean created = false;

    @Override
    public LazyClass create(SyntheticInjections lookup, Parameters params) {
        created = true;
        return new LazyClass();
    }
}
