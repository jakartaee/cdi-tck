/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.inject.Reserve;
import jakarta.enterprise.inject.Stereotype;

/**
 * Makes the bean {@code @Reserve} and declares {@code @PriorityStereotype} so it inherits {@code @Priority}
 */
@Stereotype
@Reserve
@PriorityStereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservePriorityStereotype {
}
