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
package org.jboss.cdi.tck.spi;

import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

/**
 * Provides CreationalContext related operations.
 *
 * The TCK porting package must provide an implementation of this interface which is suitable for the target implementation.
 */
public interface CreationalContexts {

    public static final String PROPERTY_NAME = CreationalContexts.class.getName();

    /**
     * Creates an {@linkplain Inspectable inspectable} CreationalContext for given Contextual.
     * This operation is identical to {@link jakarta.enterprise.inject.spi.BeanContainer#createCreationalContext(Contextual)},
     * except it returns a specialized variant of CreationalContext that can be inspected.
     *
     * @param contextual a Contextual for which a CreationalContext should be created
     * @return a CreationalContext that can be inspected
     * @param <T> type of the instance
     */
    public <T> Inspectable<T> create(Contextual<T> contextual);

    /**
     * A CreationalContext that can be inspected.
     *
     * @param <T> type of the instances
     */
    public static interface Inspectable<T> extends CreationalContext<T> {
        /**
         * Returns whether {@link #push(Object)} was called on this CreationalContext.
         *
         * @return whether {@link #push(Object)} was called on this CreationalContext.
         */
        public boolean isPushCalled();

        /**
         * If {@code push} {@linkplain #isPushCalled() was called} on this CreationalContext, returns the pushed object.
         * Returns {@code null} otherwise.
         *
         * @return the pushed object if {@code push} was called, otherwise {@code null}
         */
        public Object getLastBeanPushed();

        /**
         * Returns whether {@link #release()} was called on this CreationalContext.
         *
         * @return whether {@link #release()} was called on this CreationalContext.
         */
        public boolean isReleaseCalled();
    }

}
