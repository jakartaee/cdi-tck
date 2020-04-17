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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.modify.broken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;

public class ModifyingExtension implements Extension {

    public void modify(@Observes final ProcessBeanAttributes<Mouse> event) {

        event.setBeanAttributes(new BeanAttributes<Mouse>() {

            @SuppressWarnings("unchecked")
            public Set<Type> getTypes() {
                return Collections.unmodifiableSet(new HashSet<Type>(Arrays.asList(Object.class, Mouse.class)));
            }

            public Set<Annotation> getQualifiers() {
                return Collections.emptySet();
            }

            @Override
            public Class<? extends Annotation> getScope() {
                // Invalid attribute - a managed bean is passivation capable if and only if the bean class is serializable
                return SessionScoped.class;
            }

            public String getName() {
                return "mouse";
            }

            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.emptySet();
            }

            public boolean isAlternative() {
                return false;
            }

        });

    }

}
