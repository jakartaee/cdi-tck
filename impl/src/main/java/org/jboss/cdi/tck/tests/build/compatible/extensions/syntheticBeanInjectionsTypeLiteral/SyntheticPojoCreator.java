/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsTypeLiteral;

import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticInjections;
import jakarta.enterprise.util.TypeLiteral;

public class SyntheticPojoCreator implements SyntheticBeanCreator<SyntheticPojo> {
    @Override
    public SyntheticPojo create(SyntheticInjections injections, Parameters params) {
        Converter<String> converter = injections.get(new TypeLiteral<Converter<String>>() {
        });
        return new SyntheticPojo(converter.convert("hello"));
    }
}
