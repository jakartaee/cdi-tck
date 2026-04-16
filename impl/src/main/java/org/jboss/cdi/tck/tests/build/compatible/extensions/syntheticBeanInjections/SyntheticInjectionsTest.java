/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
public class SyntheticInjectionsTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticInjectionsTest.class)
                .withBuildCompatibleExtension(SyntheticInjectionsExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "ca", note = "withInjectionPoint(Class) registers injection points")
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "cb", note = "no qualifier passed, @Default is assumed")
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "cd", note = "SyntheticInjections used in creator")
    public void testCreatorReceivesInjections() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();
        Instance.Handle<SyntheticPojo> handle = lookup.select(SyntheticPojo.class).getHandle();
        SyntheticPojo pojo = handle.get();
        assertEquals(pojo.alphaValue, "alpha");
        assertEquals(pojo.bravoValue, "bravo");
        handle.destroy();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "ce", note = "SyntheticInjections used in disposer")
    public void testDisposerReceivesInjections() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();
        Instance.Handle<SyntheticPojo> handle = lookup.select(SyntheticPojo.class).getHandle();
        handle.get();
        SyntheticPojoDisposer.disposed.set(false);
        SyntheticPojoDisposer.bravoValueInDisposer = null;
        handle.destroy();
        assertTrue(SyntheticPojoDisposer.disposed.get());
        assertEquals(SyntheticPojoDisposer.bravoValueInDisposer, "bravo");
    }
}
