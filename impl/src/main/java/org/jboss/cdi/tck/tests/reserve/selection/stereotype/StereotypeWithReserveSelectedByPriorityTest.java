/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection.stereotype;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESERVE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_RESERVES_APPLICATION;
import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPES;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class StereotypeWithReserveSelectedByPriorityTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeWithReserveSelectedByPriorityTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = DECLARING_RESERVE, id = "ba")
    @SpecAssertion(section = DECLARING_RESERVE, id = "bb")
    @SpecAssertion(section = DECLARING_RESERVE, id = "bc")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "ca")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "cb")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "cc")
    @SpecAssertion(section = STEREOTYPES, id = "ac")
    @SpecAssertion(section = STEREOTYPES, id = "ad")
    public void testStereotypeReserveIsEnabled() {
        assertEquals(getContextualReference(SomeInterface.class).ping(), Reserve2000Impl.class.getSimpleName());
        assertEquals(getContextualReference(SomeInterface.class, ProducedByMethod.Literal.INSTANCE).ping(),
                "ReserveProducer.producer3");
        assertEquals(getContextualReference(SomeInterface.class, ProducedByField.Literal.INSTANCE).ping(),
                "ReserveProducer.producer2");
    }
}
