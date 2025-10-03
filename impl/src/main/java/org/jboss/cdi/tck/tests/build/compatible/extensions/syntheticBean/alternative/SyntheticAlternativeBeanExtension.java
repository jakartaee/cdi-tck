/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean.alternative;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class SyntheticAlternativeBeanExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn) {
        syn.addBean(SyntheticAlternativeBean.class)
                .type(SyntheticAlternativeBean.class)
                .type(TestBean.class)
                .alternative(true)
                .priority(1)
                .createWith(SyntheticAlternativeBeanCreator.class);
    }
}
