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

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class AnnotationInfoExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn) {
        syn.addBean(SyntheticPojo.class)
                .type(SyntheticPojo.class)
                .scope(Dependent.class)
                .withInjectionPoint(Widget.class, new Annotation[0])
                .withInjectionPoint(Widget.class, AnnotationBuilder.of(Nice.class).build())
                .createWith(SyntheticPojoCreator.class);
    }
}
