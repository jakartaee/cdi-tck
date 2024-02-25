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

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

public class RegisteringExtension1 implements Extension {

    void registerApple(@Observes BeforeBeanDiscovery event, final BeanManager manager) {

        new AddForwardingAnnotatedTypeAction<Apple>() {

            @Override
            public String getBaseId() {
                return RegisteringExtension1.class.getName();
            }

            @Override
            public AnnotatedType<Apple> delegate() {
                return new AnnotatedTypeWrapper<Apple>(manager.createAnnotatedType(Apple.class), false, Juicy.Literal.INSTANCE);
            }
        }.perform(event);

        new AddForwardingAnnotatedTypeAction<Orange>() {

            @Override
            public String getBaseId() {
                return RegisteringExtension1.class.getName();
            }

            @Override
            public AnnotatedType<Orange> delegate() {
                return new AnnotatedTypeWrapper<Orange>(manager.createAnnotatedType(Orange.class), false,
                        Juicy.Literal.INSTANCE);
            }
        }.perform(event);

    }
}
