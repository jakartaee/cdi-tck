/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.basic;

import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.testng.annotations.Test;

public class ReserveMetadataTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ReserveMetadataTest.class)
                .withClasses(Foo.class, Bar.class, Baz.class, Qux.class)
                .build();
    }

    @Test
    @SpecAssertion(section = BEAN, id = "be")
    public void testMetadata() {
        assertTrue(getUniqueBean(Bar.class).isReserve());
        assertFalse(getUniqueBean(Baz.class).isReserve());
        assertFalse(getUniqueBean(Qux.class).isReserve());
    }
}
