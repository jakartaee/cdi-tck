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

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;

public class SyntheticAlternativeBeanCreator implements SyntheticBeanCreator<SyntheticAlternativeBean> {
    @Override
    public SyntheticAlternativeBean create(Instance<Object> lookup, Parameters params) {
        return new SyntheticAlternativeBean();
    }
}
