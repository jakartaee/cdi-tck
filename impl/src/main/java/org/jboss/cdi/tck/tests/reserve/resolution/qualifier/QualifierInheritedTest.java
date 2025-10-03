/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.resolution.qualifier;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_RESERVES_APPLICATION;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.annotation.Inherited;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class QualifierInheritedTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(QualifierInheritedTest.class)
                .withClasses(Tree.class, Larch.class, Forest.class, True.class, True.Literal.class)
                .build();
    }

    @Inject
    private Forest forest;

    /**
     * {@link Tree} is enabled reserve and has the {@link True} qualifier that declares the {@link Inherited}
     * meta-annotation. {@link Larch} is a bean that extends {@link Tree}. Therefore the result of typesafe
     * resolution for type {@link Tree} and qualifier {@link True} is the {@link Larch} bean.
     */
    @Test
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "a")
    @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "la")
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aa")
    public void testResolution() {
        Bean<?> bean = getCurrentManager().resolve(getCurrentManager().getBeans(Tree.class, True.Literal.INSTANCE));
        assertEquals(bean.getBeanClass(), Larch.class);

        assertNotNull(forest);
        assertEquals(forest.getTree().ping(), 1);
    }
}
