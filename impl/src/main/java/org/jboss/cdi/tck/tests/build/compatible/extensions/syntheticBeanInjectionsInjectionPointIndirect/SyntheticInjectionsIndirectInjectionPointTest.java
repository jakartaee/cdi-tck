/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsInjectionPointIndirect;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Default;
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
public class SyntheticInjectionsIndirectInjectionPointTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticInjectionsIndirectInjectionPointTest.class)
                .withBuildCompatibleExtension(SyntheticInjectionsIndirectInjectionPointExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "ci", note = "InjectionPoint metadata is available for a dependent bean injected into a synthetic bean")
    public void testIndirectInjectionPoint() {
        Instance<Object> lookup = getCurrentBeanContainer().createInstance();
        Instance.Handle<SyntheticPojo> handle = lookup.select(SyntheticPojo.class).getHandle();
        SyntheticPojo pojo = handle.get();

        assertNotNull(pojo.serviceInjectionPoint);
        assertEquals(pojo.serviceInjectionPoint.getType(), MyService.class);
        assertTrue(pojo.serviceInjectionPoint.getQualifiers().contains(Default.Literal.INSTANCE));
        assertNotNull(pojo.serviceInjectionPoint.getBean());
        assertEquals(pojo.serviceInjectionPoint.getBean().getBeanClass(), SyntheticPojo.class);
        assertFalse(pojo.serviceInjectionPoint.isDelegate());
        assertFalse(pojo.serviceInjectionPoint.isTransient());
        assertNull(pojo.serviceInjectionPoint.getAnnotated());
        assertNull(pojo.serviceInjectionPoint.getMember());

        handle.destroy();
    }
}
