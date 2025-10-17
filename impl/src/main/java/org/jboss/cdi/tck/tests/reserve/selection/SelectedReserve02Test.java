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

import static org.jboss.cdi.tck.cdi.Sections.UNSATISFIED_AND_AMBIG_DEPENDENCIES;
import static org.jboss.cdi.tck.tests.reserve.selection.SelectedReserveTestUtil.createBuilderBase;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class SelectedReserve02Test extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(SelectedReserve02Test.class)
                .withClasses(Alpha.class, SimpleTestBean.class, Boss.class)
                .withBeanLibrary(Bravo.class, Foo.class)
                .withBeanLibrary(Charlie.class, Bar.class)
                .build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test
    @SpecAssertion(section = UNSATISFIED_AND_AMBIG_DEPENDENCIES, id = "cb")
    public void testDependencyResolvable() {
        assertNotNull(alpha);
        assertNotNull(bravo);
        assertNotNull(charlie);

        assertEquals(alpha.assertAvailable(TestBean.class).getId(), SimpleTestBean.class.getName());
        assertEquals(bravo.assertAvailable(TestBean.class).getId(), SimpleTestBean.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class).getId(), SimpleTestBean.class.getName());

        alpha.assertAvailable(Boss.class);
        bravo.assertAvailable(Boss.class);
        charlie.assertAvailable(Boss.class);
    }
}
