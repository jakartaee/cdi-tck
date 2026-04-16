/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsDependent;

import static org.testng.Assert.assertEquals;

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
public class SyntheticInjectionsDependentTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticInjectionsDependentTest.class)
                .withBuildCompatibleExtension(SyntheticInjectionsDependentExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.SYNTHESIS_PHASE, id = "ch", note = "@Dependent beans from SyntheticInjections destroyed with synthetic bean")
    public void testDependentBeanLifecycle() {
        TrackedBean.reset();
        SyntheticPojo.reset();

        Instance<Object> lookup = getCurrentBeanContainer().createInstance();

        assertEquals(TrackedBean.createdCount.get(), 0);
        assertEquals(TrackedBean.destroyedCount.get(), 0);

        Instance.Handle<SyntheticPojo> handle = lookup.select(SyntheticPojo.class).getHandle();
        handle.get();

        // TrackedBean was obtained during creation
        assertEquals(TrackedBean.createdCount.get(), 1);
        assertEquals(TrackedBean.destroyedCount.get(), 0);

        handle.destroy();

        // After synthetic bean destruction:
        // - TrackedBean created in creator should be destroyed
        // - TrackedBean created in disposer is also destroyed (disposer dependents
        //   are destroyed when disposal completes)
        assertEquals(TrackedBean.createdCount.get(), 2);
        assertEquals(TrackedBean.destroyedCount.get(), 2);
        assertEquals(SyntheticPojo.destroyedCount.get(), 1);
    }
}
