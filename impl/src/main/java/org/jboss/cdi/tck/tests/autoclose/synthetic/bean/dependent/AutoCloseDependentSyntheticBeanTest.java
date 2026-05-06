/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.autoclose.synthetic.bean.dependent;

import static org.jboss.cdi.tck.cdi.Sections.AUTO_CLOSING;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class AutoCloseDependentSyntheticBeanTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AutoCloseDependentSyntheticBeanTest.class)
                .withBuildCompatibleExtension(SyntheticBeanExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = AUTO_CLOSING, id = "ad")
    @SpecAssertion(section = AUTO_CLOSING, id = "b")
    public void test() {
        Instance<Object> instance = getCurrentBeanContainer().createInstance();
        ConsumerBean bean = instance.select(ConsumerBean.class).get();

        assertFalse(DependentBean.closed);
        instance.destroy(bean);
        assertTrue(DependentBean.closed);
    }
}
