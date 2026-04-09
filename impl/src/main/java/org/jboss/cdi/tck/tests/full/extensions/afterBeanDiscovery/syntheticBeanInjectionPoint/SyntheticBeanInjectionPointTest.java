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

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_CONFIGURATOR;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import jakarta.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests that InjectionPoint can be looked up via programmatic lookup
 * inside a synthetic bean's {@code produceWith} callback registered
 * through the Portable Extensions {@code AfterBeanDiscovery.addBean()} API.
 *
 * This is the PE counterpart to the BCE
 * {@code SyntheticBeanInjectionPointTest} test.
 *
 * For a {@code @Dependent} bean, the InjectionPoint should be available
 * in the {@code produceWith} callback. For a non-{@code @Dependent}
 * bean (e.g. {@code @ApplicationScoped}), or in the {@code disposeWith}
 * callback, the InjectionPoint should not be available.
 */
@SpecVersion(spec = "cdi", version = "5.0")
@Test(groups = CDI_FULL)
public class SyntheticBeanInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticBeanInjectionPointTest.class)
                .withExtension(SyntheticBeanInjectionPointExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = BEAN_CONFIGURATOR, id = "f")
    public void testInjectionPointLookupInSyntheticBean() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();

        // For historical reasons, the spec is a little loose on what should happen
        // when looking up an InjectionPoint in a synthetic bean production function
        // of a non-@Dependent bean, or in a synthetic bean destruction function.
        // Realistically, 2 different things may happen: the implementation returns
        // null, or it throws an exception. Further, that exception may be swallowed
        // by the Instance.Handle#get or destroy method. The only thing we can
        // possibly test for is whether storing the InjectionPoint lookup result
        // into a static field, whose previous value was null, changed that value.

        // @Dependent bean: InjectionPoint should be available in produceWith callback
        MyDependentBean.reset();
        Instance.Handle<MyDependentBean> handle = lookup.select(MyDependentBean.class).getHandle();
        handle.get();
        assertNotNull(MyDependentBean.produceLookup,
                "InjectionPoint lookup result should be stored for @Dependent synthetic bean");

        // @Dependent bean: InjectionPoint should NOT be available in disposeWith callback
        try {
            handle.destroy();
        } catch (Exception ignored) {
        }
        assertNull(MyDependentBean.destroyLookup,
                "InjectionPoint should not be available in disposeWith callback");

        // @ApplicationScoped bean: InjectionPoint should NOT be available in produceWith callback
        MyApplicationScopedBean.reset();
        try {
            lookup.select(MyApplicationScopedBean.class).get();
        } catch (Exception ignored) {
        }
        assertNull(MyApplicationScopedBean.produceLookup,
                "InjectionPoint should not be available for non-@Dependent synthetic bean");
    }
}
