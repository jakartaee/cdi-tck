/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection;

import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;

public final class SelectedReserveTestUtil {
    public static WebArchiveBuilder createBuilderBase() {
        return new WebArchiveBuilder()
                .withLibrary(SelectedReserveTestUtil.class, TestBean.class, Wild.class, Tame.class, AssertBean.class);
    }
}
