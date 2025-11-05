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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Types;

public class SyntheticBeanExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn, Types types) {
        syn.addBean(EagerClass.class)
                .type(EagerClass.class)
                .scope(ApplicationScoped.class)
                .eager(true)
                .createWith(EagerClassCreator.class);

        syn.addBean(LazyClass.class)
                .type(LazyClass.class)
                .scope(ApplicationScoped.class)
                .createWith(LazyClassCreator.class);

        syn.addBean(QualifiedEagerClass.class)
                .type(QualifiedEagerClass.class)
                .scope(ApplicationScoped.class)
                .qualifier(AQualifier.class)
                .eager(true)
                .createWith(QualifiedEagerClassCreator.class);

        syn.addBean(QualifiedLazyClass.class)
                .type(QualifiedLazyClass.class)
                .scope(ApplicationScoped.class)
                .qualifier(AQualifier.class)
                .createWith(QualifiedLazyClassCreator.class);
    }
}
