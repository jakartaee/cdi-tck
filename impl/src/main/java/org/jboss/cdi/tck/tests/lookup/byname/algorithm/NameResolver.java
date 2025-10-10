/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.byname.algorithm;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.inject.Inject;

@Dependent
public class NameResolver {
    @Inject
    BeanContainer container;

    public String getId() {
        Set<Bean<?>> beans = container.getBeans("mybean");
        assertFalse(beans.isEmpty());
        Bean<?> bean = container.resolve(beans);
        assertNotNull(bean);
        return bean.getBeanClass().getSimpleName();
    }
}
