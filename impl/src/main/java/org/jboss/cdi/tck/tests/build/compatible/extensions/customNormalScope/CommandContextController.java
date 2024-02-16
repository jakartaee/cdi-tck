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

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.inject.spi.BeanContainer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Allows manual activation and deactivation of the {@linkplain CommandScoped command} context.
 * The {@code activate()} method returns {@code true} if the command context was not
 * active on the current thread at the moment of the call and hence was activated by the call.
 * When the command context was active on the current thread when {@code activate()} is called,
 * {@code false} is returned and the operation is otherwise a noop.
 * <p>
 * When {@code activate()} returns {@code true}, the caller is supposed to call
 * {@code deactivate()} later on. Calling {@code deactivate()} when the command context
 * is not active leads to {@code ContextNotActiveException}. Calling {@code deactivate()}
 * when the command context is active but was not activated by this controller is a noop.
 */
public final class CommandContextController {
    private final CommandContext context;

    private final BeanContainer beanContainer;

    private final AtomicBoolean activated = new AtomicBoolean(false);

    CommandContextController(CommandContext context, BeanContainer beanContainer) {
        this.context = context;
        this.beanContainer = beanContainer;
    }

    public boolean activate() {
        try {
            beanContainer.getContext(CommandScoped.class);
            return false;
        } catch (ContextNotActiveException e) {
            context.activate();
            activated.set(true);
            return true;
        }
    }

    public void deactivate() throws ContextNotActiveException {
        beanContainer.getContext(CommandScoped.class);
        if (activated.compareAndSet(true, false)) {
            context.deactivate();
        }
    }
}
