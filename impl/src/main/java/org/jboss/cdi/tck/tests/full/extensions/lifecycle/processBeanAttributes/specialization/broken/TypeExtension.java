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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.specialization.broken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;

import org.jboss.cdi.tck.util.ForwardingBeanAttributes;

public class TypeExtension implements Extension {

    void modifySpecializingBean(@Observes ProcessBeanAttributes<Specializing> event) {
        final BeanAttributes<Specializing> delegate = event.getBeanAttributes();
        event.setBeanAttributes(new ForwardingBeanAttributes<Specializing>() {

            @Override
            public Set<Type> getTypes() {
                Set<Type> types = new HashSet<Type>();
                types.add(Object.class);
                types.add(Specializing.class);
                return Collections.unmodifiableSet(types);
            }

            @Override
            protected BeanAttributes<Specializing> attributes() {
                return delegate;
            }
        });
    }
}
