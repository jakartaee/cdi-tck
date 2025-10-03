/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean.reserve;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class SyntheticReserveBeanExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn) {
        syn.addBean(SyntheticReserveBean.class)
                .type(SyntheticReserveBean.class)
                .type(TestBean.class)
                .reserve(true)
                .priority(1)
                .createWith(SyntheticReserveBeanCreator.class);
    }
}
