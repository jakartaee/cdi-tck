/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.customContextForDependentScope;

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.AlterableContext;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

public class CustomDependentContext implements AlterableContext {
    @Override
    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        throw new UnsupportedOperationException();
    }

    public <T> T get(Contextual<T> contextual) {
        throw new UnsupportedOperationException();
    }

    public boolean isActive() {
        return true;
    }

    public void destroy(Contextual<?> contextual) {
        throw new UnsupportedOperationException();
    }
}
