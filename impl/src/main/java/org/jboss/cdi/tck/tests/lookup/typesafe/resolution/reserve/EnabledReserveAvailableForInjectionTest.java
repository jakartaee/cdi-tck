/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution.reserve;

import static org.jboss.cdi.tck.cdi.Sections.INTER_MODULE_INJECTION;
import static org.testng.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class EnabledReserveAvailableForInjectionTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnabledReserveAvailableForInjectionTest.class).build();
    }

    @Test
    @SpecAssertion(section = INTER_MODULE_INJECTION, id = "p")
    public void test() {
        assertNotNull(getContextualReference(CrabSpider.class));

        Sea sea = getContextualReference(Sea.class);
        assertNotNull(sea);
        assertNotNull(sea.crabSpider); // OK because `Sea` is `@Dependent`
    }
}
