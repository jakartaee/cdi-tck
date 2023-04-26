/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.spi;

import jakarta.enterprise.context.spi.Context;

/**
 * This interface provides operations relating to Contexts.
 *
 * The TCK porting package must provide an implementation of this interface which is suitable for the target implementation.
 *
 * @author Shane Bryzak
 * @author Pete Muir
 *
 * @param <T> The concrete context type of the implementation
 *
 */
public interface Contexts<T extends Context> {

    public static final String PROPERTY_NAME = Contexts.class.getName();

    /**
     * Sets the specified context as active.
     * <p>
     * The set of existing contextual instances of the context is preserved
     * across invocations of {@link #setActive(Context)} and {@link #setInactive(Context)}.
     *
     * @param context The context to set active
     */
    public void setActive(T context);

    /**
     * Sets the specified context as inactive.
     * <p>
     * The set of existing contextual instances of the context is preserved
     * across invocations of {@link #setActive(Context)} and {@link #setInactive(Context)}.
     *
     * @param context The context to set inactive
     */
    public void setInactive(T context);

    /**
     * Get the request context, regardless of whether it is active or not
     *
     * @return The request context
     */
    public T getRequestContext();

    /**
     * Returns the dependent context, regardless of whether it is active or not
     *
     * @return the dependent context
     */
    public T getDependentContext();

    /**
     * Destroy the context. This operation is defined by the CDI specification but has no API.
     *
     * @param context the context to destroy
     */
    public void destroyContext(T context);

}
