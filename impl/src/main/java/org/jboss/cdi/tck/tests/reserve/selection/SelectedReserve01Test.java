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

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESERVE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_RESERVES_APPLICATION;
import static org.jboss.cdi.tck.tests.reserve.selection.SelectedReserveTestUtil.createBuilderBase;
import static org.testng.Assert.assertEquals;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class SelectedReserve01Test extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(SelectedReserve01Test.class)
                .withClasses(Alpha.class)
                .withBeanLibrary(Bravo.class, Foo.class)
                .withBeanLibrary(Charlie.class, Bar.class, BarProducer.class)
                .build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test
    @SpecAssertion(section = DECLARING_RESERVE, id = "aa")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "a")
    public void testReserveManagedBeanSelected() {
        alpha.assertAvailable(Foo.class);
        bravo.assertAvailable(Foo.class);
        charlie.assertAvailable(Foo.class);

        alpha.assertAvailable(Bar.class);
        bravo.assertAvailable(Bar.class);
        charlie.assertAvailable(Bar.class);

        assertEquals(alpha.assertAvailable(TestBean.class).getId(), Bar.class.getName());
        assertEquals(bravo.assertAvailable(TestBean.class).getId(), Bar.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class).getId(), Bar.class.getName());
    }

    @Test
    @SpecAssertion(section = DECLARING_RESERVE, id = "ab")
    @SpecAssertion(section = DECLARING_RESERVE, id = "ac")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "ba")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "bb")
    public void testReserveProducerSelected() {
        // Producer field
        alpha.assertAvailable(Bar.class, Wild.Literal.INSTANCE);
        bravo.assertAvailable(Bar.class, Wild.Literal.INSTANCE);
        charlie.assertAvailable(Bar.class, Wild.Literal.INSTANCE);

        assertEquals(alpha.assertAvailable(TestBean.class, Wild.Literal.INSTANCE).getId(), Bar.class.getName());
        assertEquals(bravo.assertAvailable(TestBean.class, Wild.Literal.INSTANCE).getId(), Bar.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class, Wild.Literal.INSTANCE).getId(), Bar.class.getName());

        // Producer method
        alpha.assertAvailable(Bar.class, Tame.Literal.INSTANCE);
        bravo.assertAvailable(Bar.class, Tame.Literal.INSTANCE);
        charlie.assertAvailable(Bar.class, Tame.Literal.INSTANCE);

        assertEquals(alpha.assertAvailable(TestBean.class, Tame.Literal.INSTANCE).getId(), Bar.class.getName());
        assertEquals(bravo.assertAvailable(TestBean.class, Tame.Literal.INSTANCE).getId(), Bar.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class, Tame.Literal.INSTANCE).getId(), Bar.class.getName());
    }
}
