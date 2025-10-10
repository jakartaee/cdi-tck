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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
public class QualifierNotDeclaredTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(QualifierNotDeclaredTest.class)
                .withClasses(Foo.class, Bar.class, Baz.class, Qux.class, True.class, True.Literal.class)
                .build();
    }

    @Inject
    private Qux qux;

    /**
     * {@link Bar} is an enabled reserve that implements {@link Foo} and has the {@link True} qualifier. {@link Baz} is
     * a bean that implements {@link Foo} but does not have {@link True}. Therefore the result of typesafe resolution
     * for type {@link Foo} and qualifier {@link True} is {@link Bar}.
     */
    @Test
    @SpecAssertion(section = DECLARING_SELECTED_RESERVES_APPLICATION, id = "a")
    @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "lb")
    public void testResolution() {
        Bean<?> bean = getCurrentManager().resolve(getCurrentManager().getBeans(Foo.class, True.Literal.INSTANCE));
        assertEquals(bean.getBeanClass(), Bar.class);

        assertNotNull(qux);
        assertEquals(qux.getFoo().ping(), 1);
    }
}
