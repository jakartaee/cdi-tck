/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsAnnotationInfo;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticInjections;

public class SyntheticPojoCreator implements SyntheticBeanCreator<SyntheticPojo> {
    @Override
    public SyntheticPojo create(SyntheticInjections injections, Parameters params) {
        Widget plain = injections.get(Widget.class);
        Widget nice = injections.get(Widget.class, Nice.Literal.INSTANCE);
        return new SyntheticPojo(plain.name(), nice.name());
    }
}
