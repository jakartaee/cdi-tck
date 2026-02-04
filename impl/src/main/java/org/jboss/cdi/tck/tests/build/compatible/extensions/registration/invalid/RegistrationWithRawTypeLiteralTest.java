/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.registration.invalid;

import jakarta.enterprise.inject.spi.DefinitionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class RegistrationWithRawTypeLiteralTest extends AbstractTest {
    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(RegistrationWithRawTypeLiteralTest.class)
                .withBuildCompatibleExtension(RegistrationWithRawTypeLiteralExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.REGISTRATION_PHASE, id = "ac", note = "Invalid BeanInfo filtering")
    public void trigger() {
    }
}
