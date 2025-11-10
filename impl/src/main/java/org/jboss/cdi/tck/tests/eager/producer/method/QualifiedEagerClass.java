/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.eager.producer.method;

public class QualifiedEagerClass {
    public static boolean constructed = false;

    public QualifiedEagerClass() {
    }

    // the parameter only exists to distinguish a non-default constructor
    // that is used to create the contextual instance
    // (the client proxy uses the default constructor)
    public QualifiedEagerClass(int unused) {
        constructed = true;
    }
}
