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

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.util.TypeLiteral;

public class RegistrationWithRawTypeLiteralExtension implements BuildCompatibleExtension {
    @Registration(types = MyRawTypeLiteral.class)
    public void wrong(BeanInfo bean) {
    }

    static class MyRawTypeLiteral extends TypeLiteral {
    }
}
