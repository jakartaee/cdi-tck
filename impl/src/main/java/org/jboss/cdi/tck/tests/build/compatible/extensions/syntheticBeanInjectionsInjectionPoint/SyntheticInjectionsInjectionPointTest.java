/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsInjectionPoint;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import jakarta.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class SyntheticInjectionsInjectionPointTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticInjectionsInjectionPointTest.class)
                .withBuildCompatibleExtension(SyntheticInjectionsInjectionPointExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "ci", note = "@Dependent synthetic bean can obtain InjectionPoint from SyntheticInjections")
    public void testInjectionPointLookup() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();
        Instance.Handle<PojoConsumer> handle = lookup.select(PojoConsumer.class).getHandle();
        PojoConsumer consumer = handle.get();
        assertNotNull(consumer.pojo);
        assertNotNull(consumer.pojo.injectionPoint);
        assertEquals(consumer.pojo.injectionPoint.getType(), SyntheticPojo.class);
        handle.destroy();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "ci", note = "InjectionPoint lookup in disposer via SyntheticInjections is invalid")
    public void testDisposerInjectionPointLookupIsInvalid() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();

        SyntheticPojoDisposer.lookedUp = null;

        Instance.Handle<PojoConsumer> handle = lookup.select(PojoConsumer.class).getHandle();
        handle.get();

        handle.destroy();

        assertNull(SyntheticPojoDisposer.lookedUp);
    }
}
