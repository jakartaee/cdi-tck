/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.autoclose.producer.method;

import static org.jboss.cdi.tck.cdi.Sections.AUTO_CLOSING;
import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class AutoCloseProducerMethodTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AutoCloseProducerMethodTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = AUTO_CLOSING, id = "ab")
    @SpecAssertion(section = AUTO_CLOSING, id = "b")
    public void testWithAutoClose() {
        Bean<WithAutoClose> bean = getUniqueBean(WithAutoClose.class);
        CreationalContext<WithAutoClose> cc = getCurrentBeanContainer().createCreationalContext(bean);
        WithAutoClose instance = bean.create(cc);
        assertFalse(Producers.withAutoCloseDisposed);
        assertFalse(instance.closed);
        bean.destroy(instance, cc);
        assertTrue(Producers.withAutoCloseDisposed);
        assertTrue(instance.closed);
    }

    @Test
    @SpecAssertion(section = AUTO_CLOSING, id = "ab")
    @SpecAssertion(section = AUTO_CLOSING, id = "b")
    public void testWithoutAutoClose() {
        Bean<WithoutAutoClose> bean = getUniqueBean(WithoutAutoClose.class);
        CreationalContext<WithoutAutoClose> cc = getCurrentBeanContainer().createCreationalContext(bean);
        WithoutAutoClose instance = bean.create(cc);
        assertFalse(Producers.withoutAutoCloseDisposed);
        bean.destroy(instance, cc);
        assertTrue(Producers.withoutAutoCloseDisposed);
    }

    @Test
    @SpecAssertion(section = BEAN, id = "bg")
    public void testMetadata() {
        assertTrue(getUniqueBean(WithAutoClose.class).isAutoClose());
        assertTrue(getUniqueBean(WithoutAutoClose.class).isAutoClose());
    }
}
