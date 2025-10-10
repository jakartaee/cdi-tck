/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.definition.stereotype.broken.alternativeReserve;

import static org.jboss.cdi.tck.cdi.Sections.RESERVE_STEREOTYPE;

import jakarta.enterprise.inject.spi.DefinitionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class AlternativeAndReserveOnTwoStereotypesTest extends AbstractTest {
    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static WebArchive deploy() {
        return new WebArchiveBuilder()
                .withTestClass(AlternativeAndReserveOnTwoStereotypesTest.class)
                .withClasses(AlternativeStereotype.class, ReserveStereotype.class, SomeOtherBean.class)
                .build();
    }

    @Test
    @SpecAssertion(section = RESERVE_STEREOTYPE, id = "b")
    public void trigger() {
    }
}
