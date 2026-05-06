/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.autoclose.synthetic.bean;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class SyntheticBeanExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn) {
        syn.addBean(WithAutoClose.class)
                .type(WithAutoClose.class)
                .scope(Dependent.class)
                .autoClose(true)
                .createWith(WithAutoCloseCreator.class)
                .disposeWith(WithAutoCloseDisposer.class);

        syn.addBean(WithoutAutoClose.class)
                .type(WithoutAutoClose.class)
                .scope(Dependent.class)
                .autoClose(true)
                .createWith(WithoutAutoCloseCreator.class)
                .disposeWith(WithoutAutoCloseDisposer.class);
    }
}
