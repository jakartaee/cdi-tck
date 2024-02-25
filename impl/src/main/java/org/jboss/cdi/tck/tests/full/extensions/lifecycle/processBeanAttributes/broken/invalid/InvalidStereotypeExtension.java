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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.broken.invalid;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;

import org.jboss.cdi.tck.util.ForwardingBeanAttributes;

public class InvalidStereotypeExtension implements Extension {

    public void modify(@Observes ProcessBeanAttributes<Telephone> event) {
        final BeanAttributes<Telephone> delegate = event.getBeanAttributes();
        event.setBeanAttributes(new ForwardingBeanAttributes<Telephone>() {

            @Override
            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.<Class<? extends Annotation>> singleton(PlainOldAnnotation.class);
            }

            @Override
            protected BeanAttributes<Telephone> attributes() {
                return delegate;
            }
        });
    }
}