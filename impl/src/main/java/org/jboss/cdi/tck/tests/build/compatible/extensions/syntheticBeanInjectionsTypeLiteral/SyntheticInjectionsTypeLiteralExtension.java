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

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Types;
import jakarta.enterprise.lang.model.types.Type;

public class SyntheticInjectionsTypeLiteralExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn, Types types) {
        // Build Converter<String> as a lang model Type
        Type converterOfString = types.parameterized(Converter.class, String.class);

        syn.addBean(SyntheticPojo.class)
                .type(SyntheticPojo.class)
                .scope(Dependent.class)
                .withInjectionPoint(converterOfString, new Annotation[0])
                .createWith(SyntheticPojoCreator.class);
    }
}
