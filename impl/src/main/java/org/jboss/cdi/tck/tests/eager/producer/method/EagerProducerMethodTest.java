/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.eager.producer.method;

import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.jboss.cdi.tck.cdi.Sections.EAGER_INITIALIZATION;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class EagerProducerMethodTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EagerProducerMethodTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "ab")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "b")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "c")
    public void testUnqualified() {
        assertTrue(EagerClass.constructed);

        assertFalse(LazyClass.constructed);
        LazyClass reference = getContextualReference(LazyClass.class);
        assertNotNull(reference);
        assertFalse(LazyClass.constructed);
        assertNotNull(reference.toString());
        assertTrue(LazyClass.constructed);
    }

    @Test
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "ab")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "b")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "c")
    public void testQualified() {
        assertTrue(QualifiedEagerClass.constructed);

        assertFalse(QualifiedLazyClass.constructed);
        QualifiedLazyClass reference = getContextualReference(QualifiedLazyClass.class, AQualifier.Literal.INSTANCE);
        assertNotNull(reference);
        assertFalse(QualifiedLazyClass.constructed);
        assertNotNull(reference.toString());
        assertTrue(QualifiedLazyClass.constructed);
    }

    @Test
    @SpecAssertion(section = BEAN, id = "bf")
    public void testMetadata() {
        assertTrue(getUniqueBean(EagerClass.class).isEager());
        assertTrue(getUniqueBean(QualifiedEagerClass.class, AQualifier.Literal.INSTANCE).isEager());

        assertFalse(getUniqueBean(LazyClass.class).isEager());
        assertFalse(getUniqueBean(QualifiedLazyClass.class, AQualifier.Literal.INSTANCE).isEager());
    }
}
