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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Reserve;
import jakarta.enterprise.inject.Stereotype;

@Stereotype
@Alternative
@Reserve
@Priority(1)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AlternativeReserveStereotype {
}
