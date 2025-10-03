/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

public abstract class AssertBean {
    @Inject
    @Any
    Instance<Object> lookup;

    @Inject
    BeanManager beanManager;

    /**
     * Assert that a bean with given type and qualifiers is available for injection.
     *
     * @return the bean instance
     */
    public <T> T assertAvailable(Class<T> beanType, Annotation... qualifiers) {
        assertNotNull(beanManager.resolve(beanManager.getBeans(beanType, qualifiers)));

        Instance<T> beanLookup = lookup.select(beanType, qualifiers);
        assertTrue(beanLookup.isResolvable());
        T beanInstance = beanLookup.get();
        assertNotNull(beanInstance);
        return beanInstance;
    }
}
