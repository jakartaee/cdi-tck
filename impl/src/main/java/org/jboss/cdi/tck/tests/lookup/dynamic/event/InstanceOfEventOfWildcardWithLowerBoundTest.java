/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.dynamic.event;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class InstanceOfEventOfWildcardWithLowerBoundTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(InstanceOfEventOfWildcardWithLowerBoundTest.class)
                .withClasses(InstanceOfEventOfWildcardWithLowerBound.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DYNAMIC_LOOKUP, id = "aba")
    @SpecAssertion(section = Sections.EVENT, id = "cbb")
    public void test() {
        getContextualReference(InstanceOfEventOfWildcardWithLowerBound.class).fire();
        assertEquals(InstanceOfEventOfWildcardWithLowerBound.seen, List.of("hello", "world", "hi", "there"));
    }
}
