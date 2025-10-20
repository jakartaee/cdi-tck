/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution.broken.disabled.reserve.producer;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Reserve;

@Reserve
@Dependent
public class CrabSpiderProducer {
    // the declaring bean is disabled, so this producer is also disabled
    @Produces
    public CrabSpider create() {
        return new CrabSpider();
    }
}
