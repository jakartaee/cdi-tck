/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection.priority;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESERVE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_RESERVES_APPLICATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class ReservePriorityTest extends AbstractTest {
    public static final String RESERVE = "reserve";
    public static final String RESERVE2 = "reserve2";
    public static final String DEFAULT = "default";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ReservePriorityTest.class).build();
    }

    @Inject
    @ProducedByMethod
    Alpha alphaMethodProducer;

    @Inject
    @ProducedByField
    Alpha alphaFieldProducer;

    @Inject
    @ProducedByMethod
    Beta betaMethodProducer;

    @Inject
    @ProducedByField
    Beta betaFieldProducer;

    @Inject
    @ProducedByMethod
    Gamma gammaMethodProducer;

    @Inject
    @ProducedByField
    Gamma gammaFieldProducer;

    @Inject
    @ProducedByMethod
    Delta deltaMethodProducer;

    @Inject
    @ProducedByField
    Delta deltaFieldProducer;

    @Test
    @SpecAssertion(section = DECLARING_RESERVE, id = "ab")
    @SpecAssertion(section = DECLARING_RESERVE, id = "ac")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "ba")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "bb")
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "d")
    public void testReserveProducer() {
        assertNotNull(alphaMethodProducer);
        assertNotNull(alphaFieldProducer);
        assertNotNull(betaMethodProducer);
        assertNotNull(betaFieldProducer);
        assertNotNull(gammaMethodProducer);
        assertNotNull(gammaFieldProducer);
        assertNotNull(deltaFieldProducer);
        assertNotNull(deltaMethodProducer);

        assertEquals(alphaMethodProducer.ping(), RESERVE);
        assertEquals(alphaFieldProducer.ping(), RESERVE);
        assertEquals(betaMethodProducer.ping(), RESERVE2);
        assertEquals(betaFieldProducer.ping(), RESERVE2);
        assertEquals(gammaMethodProducer.ping(), RESERVE);
        assertEquals(gammaFieldProducer.ping(), RESERVE);
        assertEquals(deltaFieldProducer.ping(), DEFAULT);
        assertEquals(deltaMethodProducer.ping(), DEFAULT);
    }
}
