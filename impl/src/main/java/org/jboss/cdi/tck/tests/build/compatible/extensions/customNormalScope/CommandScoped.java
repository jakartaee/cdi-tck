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
package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.context.NormalScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a bean belongs to the <em>command</em> normal scope.
 * <p>
 * A dependent-scoped bean of type {@link CommandContextController CommandContextController}
 * is provided that may be used to manually activate and deactivate the command context.
 * <p>
 * A dependent-scoped bean of type {@link CommandExecutor CommandExecutor} is provided that
 * may be used to execute a {@link Command Command} implementation, activating and deactivating
 * the command scope automatically.
 * <p>
 * A command-scoped bean of type {@link CommandExecution CommandExecution} is provided that contains
 * certain details about the command execution and allows exchanging data between beans in the same command scope.
 */
@NormalScope
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CommandScoped {
}
