/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.byname.algorithm;

import static org.jboss.cdi.tck.cdi.Sections.AMBIG_NAMES;
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
public class Step7bTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(Step7bTest.class)
                .withClasses(NameResolver.class, Reserve1000A.class, Reserve1000B.class, Reserve2000.class,
                        Classic.class, Alternative1000A.class, Alternative1000B.class, Alternative2000.class)
                .build();
    }

    @Inject
    NameResolver nameResolver;

    @Test
    @SpecAssertion(section = AMBIG_NAMES, id = "cg")
    public void test() {
        assertNotNull(nameResolver);
        assertEquals(nameResolver.getId(), "Alternative2000");
    }
}
