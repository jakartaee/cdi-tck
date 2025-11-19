/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.eager.bean;

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
public class EagerBeanTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EagerBeanTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "aa")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "b")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "c")
    public void testUnqualified() {
        assertTrue(EagerBean.constructed);

        assertFalse(LazyBean.constructed);
        LazyBean reference = getContextualReference(LazyBean.class);
        assertNotNull(reference);
        assertFalse(LazyBean.constructed);
        assertNotNull(reference.toString());
        assertTrue(LazyBean.constructed);
    }

    @Test
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "aa")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "b")
    @SpecAssertion(section = EAGER_INITIALIZATION, id = "c")
    public void testQualified() {
        assertTrue(QualifiedEagerBean.constructed);

        assertFalse(QualifiedLazyBean.constructed);
        QualifiedLazyBean reference = getContextualReference(QualifiedLazyBean.class, AQualifier.Literal.INSTANCE);
        assertNotNull(reference);
        assertFalse(QualifiedLazyBean.constructed);
        assertNotNull(reference.toString());
        assertTrue(QualifiedLazyBean.constructed);
    }

    @Test
    @SpecAssertion(section = BEAN, id = "bf")
    public void testMetadata() {
        assertTrue(getUniqueBean(EagerBean.class).isEager());
        assertTrue(getUniqueBean(QualifiedEagerBean.class, AQualifier.Literal.INSTANCE).isEager());

        assertFalse(getUniqueBean(LazyBean.class).isEager());
        assertFalse(getUniqueBean(QualifiedLazyBean.class, AQualifier.Literal.INSTANCE).isEager());
    }
}
