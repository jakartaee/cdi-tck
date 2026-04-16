/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsUnregistered;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticInjections;

public class SyntheticPojoCreator implements SyntheticBeanCreator<SyntheticPojo> {
    static boolean illegalArgumentThrown = false;

    @Override
    public SyntheticPojo create(SyntheticInjections injections, Parameters params) {
        // This should work — RegisteredBean was registered
        injections.get(RegisteredBean.class);

        // This should throw IllegalArgumentException — UnregisteredBean was NOT registered
        try {
            injections.get(UnregisteredBean.class);
        } catch (IllegalArgumentException e) {
            illegalArgumentThrown = true;
        }

        return new SyntheticPojo();
    }
}
