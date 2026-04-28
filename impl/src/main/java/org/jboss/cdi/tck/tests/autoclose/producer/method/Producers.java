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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class Producers {
    static boolean withAutoCloseDisposed = false;
    static boolean withoutAutoCloseDisposed = false;

    @Produces
    @Dependent
    @AutoClose
    public WithAutoClose withAutoClose() {
        return new WithAutoClose();
    }

    @Produces
    @Dependent
    @AutoClose
    public WithoutAutoClose withoutAutoClose() {
        return new WithoutAutoClose();
    }

    public void disposeWithAutoClose(@Disposes WithAutoClose withAutoClose) {
        withAutoCloseDisposed = true;
    }

    public void disposeWithoutAutoClose(@Disposes WithoutAutoClose withAutoClose) {
        withoutAutoCloseDisposed = true;
    }
}
