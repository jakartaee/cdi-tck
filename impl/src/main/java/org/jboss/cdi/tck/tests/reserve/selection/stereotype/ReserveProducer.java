/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.reserve.selection.stereotype;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ReserveProducer {
    @ReserveStereotype
    @ProducedByMethod
    @Produces
    SomeInterface producer1() {
        return new SomeInterface() {
            @Override
            public String ping() {
                return "ReserveProducer.producer1";
            }
        };
    }

    @ReserveStereotype
    @ProducedByField
    @Produces
    SomeInterface producer2 = new SomeInterface() {
        @Override
        public String ping() {
            return "ReserveProducer.producer2";
        }
    };

    @ReserveStereotype
    @Priority(2000)
    @ProducedByMethod
    @Produces
    SomeInterface producer3() {
        return new SomeInterface() {
            @Override
            public String ping() {
                return "ReserveProducer.producer3";
            }
        };
    }

    @ReserveStereotype
    @Priority(500)
    @ProducedByField
    @Produces
    SomeInterface producer4 = new SomeInterface() {
        @Override
        public String ping() {
            return "ReserveProducer.producer4";
        }
    };
}
