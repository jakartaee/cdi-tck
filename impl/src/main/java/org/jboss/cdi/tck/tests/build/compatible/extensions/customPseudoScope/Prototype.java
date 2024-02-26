/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.customPseudoScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.inject.Scope;

/**
 * Specifies that a bean belongs to the <em>prototype</em> pseudo-scope.
 * <p>
 * When a bean is declared to have the {@code @Prototype} scope:
 * <ul>
 * <li>Each injection point or dynamic lookup receives a new instance; instances are never shared.</li>
 * <li>Lifecycle of instances is not managed by the CDI container.</li>
 * </ul>
 * <p>
 * Every invocation of the {@link Context#get(Contextual, CreationalContext)} operation on the
 * context object for the {@code @Prototype} scope returns a new instance of given bean.
 * <p>
 * Every invocation of the {@link Context#get(Contextual)} operation on the context object for the
 * {@code @Prototype} scope returns a {@code null} value.
 * <p>
 * The {@code @Prototype} scope is always active.
 */
@Scope
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Prototype {
}
