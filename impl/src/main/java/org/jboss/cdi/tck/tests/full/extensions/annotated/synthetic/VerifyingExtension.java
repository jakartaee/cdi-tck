/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.annotated.synthetic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessSyntheticAnnotatedType;

public class VerifyingExtension implements Extension {

    private Set<Class<?>> patClasses = new HashSet<Class<?>>();
    private Set<Class<?>> psatClasses = new HashSet<Class<?>>();
    private Map<Class<?>, Extension> sources = new HashMap<Class<?>, Extension>();

    public <T> void verify(@Observes ProcessAnnotatedType<T> event) {
        if (event instanceof ProcessSyntheticAnnotatedType<?>) {
            psatClasses.add(event.getAnnotatedType().getJavaClass());
        } else {
            patClasses.add(event.getAnnotatedType().getJavaClass());
        }
    }

    public <T extends Fruit> void verifySource(@Observes ProcessSyntheticAnnotatedType<T> event) {
        sources.put(event.getAnnotatedType().getJavaClass(), event.getSource());
    }

    protected Set<Class<?>> getPatClasses() {
        return patClasses;
    }

    protected Set<Class<?>> getPsatClasses() {
        return psatClasses;
    }

    protected Map<Class<?>, Extension> getSources() {
        return sources;
    }
}
