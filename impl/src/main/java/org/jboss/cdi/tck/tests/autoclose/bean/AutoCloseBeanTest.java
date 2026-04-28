/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.autoclose.bean;

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
public class AutoCloseBeanTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AutoCloseBeanTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = AUTO_CLOSING, id = "aa")
    @SpecAssertion(section = AUTO_CLOSING, id = "b")
    public void testAutoClose() {
        Bean<WithAutoCloseBean> bean = getUniqueBean(WithAutoCloseBean.class);
        CreationalContext<WithAutoCloseBean> cc = getCurrentBeanContainer().createCreationalContext(bean);
        WithAutoCloseBean instance = bean.create(cc);
        assertFalse(instance.closed);
        bean.destroy(instance, cc);
        assertTrue(instance.destroyed);
        assertTrue(instance.closed);
    }

    @Test
    @SpecAssertion(section = AUTO_CLOSING, id = "aa")
    @SpecAssertion(section = AUTO_CLOSING, id = "b")
    public void testNoAutoClose() {
        Bean<WithoutAutoCloseBean> bean = getUniqueBean(WithoutAutoCloseBean.class);
        CreationalContext<WithoutAutoCloseBean> cc = getCurrentBeanContainer().createCreationalContext(bean);
        WithoutAutoCloseBean instance = bean.create(cc);
        bean.destroy(instance, cc);
        assertTrue(instance.destroyed);
    }

    @Test
    @SpecAssertion(section = BEAN, id = "bg")
    public void testMetadata() {
        assertTrue(getUniqueBean(WithAutoCloseBean.class).isAutoClose());
        assertTrue(getUniqueBean(WithoutAutoCloseBean.class).isAutoClose());
    }
}
