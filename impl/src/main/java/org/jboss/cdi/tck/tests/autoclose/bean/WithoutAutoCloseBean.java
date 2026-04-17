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

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.context.Dependent;

@Dependent
@AutoClose
public class WithoutAutoCloseBean {
    public boolean destroyed = false;

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }
}
