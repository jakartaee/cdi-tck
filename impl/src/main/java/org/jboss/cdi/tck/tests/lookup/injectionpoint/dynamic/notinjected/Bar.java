/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.injectionpoint.dynamic.notinjected;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.inject.Inject;

@Dependent
public class Bar {
    @Inject
    BeanContainer container;

    public Foo getFoo() {
        return container.createInstance().select(Foo.class).get();
    }

    public Foo getNiceFooByType() {
        return container.createInstance().select(NiceFoo.class, Any.Literal.INSTANCE).get();
    }

    public Foo getNiceFooByQualifier() {
        return container.createInstance().select(Foo.class, new Nice.Literal()).get();
    }

    public Foo getNiceFooByAllQualifiers() {
        return container.createInstance().select(Foo.class, Any.Literal.INSTANCE, new Nice.Literal()).get();
    }
}
