/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.util;

import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;

/**
 * Tests should use this abstraction when adding new custom {@link AnnotatedType} during {@link BeforeBeanDiscovery}
 * notification.
 *
 * See also CDI-58.
 *
 * @author Martin Kouba
 */
public abstract class AddForwardingAnnotatedTypeAction<X> extends ForwardingAnnotatedType<X> implements AnnotatedType<X> {

    public abstract String getBaseId();

    public String getId() {
        return buildId(getBaseId(), delegate().getJavaClass());
    }

    public void perform(BeforeBeanDiscovery event) {
        event.addAnnotatedType(this, getId());
    }

    public static String buildId(String baseId, Class<?> javaClass) {
        return baseId + "_" + javaClass.getName();
    }

}
