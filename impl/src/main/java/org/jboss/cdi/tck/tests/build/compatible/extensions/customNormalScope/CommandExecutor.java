/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

/**
 * Executes a {@link Command Command} in the command scope. That is, the command context
 * is activated before calling {@code Command.execute()} and deactivated when
 * {@code Command.execute()} returns (or throws).
 */
@Dependent
public class CommandExecutor {
    private final CommandContextController control;

    @Inject
    CommandExecutor(CommandContextController control) {
        this.control = control;
    }

    public void execute(Command command) {
        try {
            control.activate();
            command.execute();
        } finally {
            control.deactivate();
        }
    }
}
