/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.full.extensions.afterBeanDiscovery.syntheticBeanInjectionPoint;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class SyntheticBeanInjectionPointExtension implements Extension {

    public void addBeans(@Observes AfterBeanDiscovery event) {
        // @Dependent synthetic bean - InjectionPoint lookup should succeed
        event.addBean()
                .beanClass(MyDependentBean.class)
                .addType(MyDependentBean.class)
                .scope(Dependent.class)
                .produceWith(instance -> {
                    MyDependentBean.produceLookup = instance.select(InjectionPoint.class).get();
                    return new MyDependentBean();
                })
                .disposeWith((bean, instance) -> {
                    try {
                        MyDependentBean.destroyLookup = instance.select(InjectionPoint.class).get();
                    } catch (Exception e) {
                        // lookup failed with an exception
                    }
                });

        // @ApplicationScoped synthetic bean - InjectionPoint lookup should not succeed
        event.addBean()
                .beanClass(MyApplicationScopedBean.class)
                .addType(MyApplicationScopedBean.class)
                .scope(ApplicationScoped.class)
                .produceWith(instance -> {
                    try {
                        MyApplicationScopedBean.produceLookup = instance.select(InjectionPoint.class).get();
                    } catch (Exception e) {
                        // lookup failed with an exception
                    }
                    return new MyApplicationScopedBean();
                });
    }
}
