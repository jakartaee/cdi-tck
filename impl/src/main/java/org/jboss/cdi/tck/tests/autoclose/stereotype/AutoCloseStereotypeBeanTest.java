/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.autoclose.stereotype;

import static org.jboss.cdi.tck.cdi.Sections.AUTO_CLOSE_STEREOTYPE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN;
import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPES;
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
public class AutoCloseStereotypeBeanTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AutoCloseStereotypeBeanTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = STEREOTYPES, id = "af")
    @SpecAssertion(section = AUTO_CLOSE_STEREOTYPE, id = "a")
    public void testWithAutoClose() {
        Bean<WithAutoCloseBean> bean = getUniqueBean(WithAutoCloseBean.class);
        CreationalContext<WithAutoCloseBean> cc = getCurrentBeanContainer().createCreationalContext(bean);
        WithAutoCloseBean instance = bean.create(cc);
        assertFalse(instance.closed);
        assertFalse(instance.destroyed);
        bean.destroy(instance, cc);
        assertTrue(instance.destroyed);
        assertTrue(instance.closed);
    }

    @Test
    @SpecAssertion(section = STEREOTYPES, id = "af")
    @SpecAssertion(section = AUTO_CLOSE_STEREOTYPE, id = "a")
    public void testWithoutAutoClose() {
        Bean<WithoutAutoCloseBean> bean = getUniqueBean(WithoutAutoCloseBean.class);
        CreationalContext<WithoutAutoCloseBean> cc = getCurrentBeanContainer().createCreationalContext(bean);
        WithoutAutoCloseBean instance = bean.create(cc);
        assertFalse(instance.destroyed);
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
