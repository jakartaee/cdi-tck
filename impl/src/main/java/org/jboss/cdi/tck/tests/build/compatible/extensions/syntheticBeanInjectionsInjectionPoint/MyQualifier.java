/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionsInjectionPoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface MyQualifier {
    String binding();

    @Nonbinding
    String nonBinding();

    final class Literal extends AnnotationLiteral<MyQualifier> implements MyQualifier {
        private final String binding;
        private final String nonBinding;

        public Literal(String binding, String nonBinding) {
            this.binding = binding;
            this.nonBinding = nonBinding;
        }

        @Override
        public String binding() {
            return binding;
        }

        @Override
        public String nonBinding() {
            return nonBinding;
        }
    }
}
