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

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;

/**
 * Provides Contextual related operations.
 *
 * The TCK porting package must provide an implementation of this interface which is suitable for the target implementation.
 */
public interface Contextuals {

    public static final String PROPERTY_NAME = Contextuals.class.getName();

    /**
     * Creates a dummy {@linkplain Inspectable inspectable} Contextual that will be used
     * with given Context. The result does not necessarily fulfil the entire {@link Contextual}
     * contract; the TCK only requires it to:
     *
     * <ul>
     * <li>return the given {@code instance} from {@link Contextual#create(CreationalContext)};</li>
     * <li>capture all the parameters passed to {@link Contextual} methods in order to fulfil
     * the {@link Inspectable} contract.</li>
     * </ul>
     *
     * @param instance the instance to be returned by {@link Contextual#create(CreationalContext)}
     * @param context the Context that the returned Contextual can be used with
     * @return a Contextual that can be inspected
     * @param <T> type of the instance
     */
    public <T> Inspectable<T> create(T instance, Context context);

    /**
     * A Contextual that can be inspected.
     *
     * @param <T> type of the instances
     */
    public static interface Inspectable<T> extends Contextual<T> {
        /**
         * Returns the {@link CreationalContext} passed to {@link Contextual#create(CreationalContext)}.
         * Returns {@code null} if {@code create} was not called yet.
         *
         * @return the {@link CreationalContext} passed to {@link Contextual#create(CreationalContext)}
         */
        public CreationalContext<T> getCreationalContextPassedToCreate();

        /**
         * Returns the instance passed to {@link Contextual#destroy(Object, CreationalContext)}.
         * Returns {@code null} if {@code destroy} was not called yet.
         *
         * @return the instance passed to {@link Contextual#destroy(Object, CreationalContext)}
         */
        public T getInstancePassedToDestroy();

        /**
         * Returns the {@link CreationalContext} passed to {@link Contextual#destroy(Object, CreationalContext)}.
         * Returns {@code null} if {@code destroy} was not called yet.
         *
         * @return the {@link CreationalContext} passed to {@link Contextual#destroy(Object, CreationalContext)}
         */
        public CreationalContext<T> getCreationalContextPassedToDestroy();
    }

}
