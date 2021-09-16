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
package org.jboss.cdi.tck.tests.full.extensions.annotated.delivery;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

public class BeforeBeanDiscoveryObserver implements Extension {

    public void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, final BeanManager beanManager) {

        new AddForwardingAnnotatedTypeAction<Phoenix>() {

            @Override
            public String getBaseId() {
                return BeforeBeanDiscoveryObserver.class.getName();
            }

            @Override
            public AnnotatedType<Phoenix> delegate() {
                return beanManager.createAnnotatedType(Phoenix.class);
            }
        }.perform(event);

        new AddForwardingAnnotatedTypeAction<Griffin>() {

            @Override
            public String getBaseId() {
                return BeforeBeanDiscoveryObserver.class.getName();
            }

            @Override
            public AnnotatedType<Griffin> delegate() {
                return new AnnotatedTypeWrapper<Griffin>(beanManager.createAnnotatedType(Griffin.class), false,
                        Wanted.WantedLiteral.INSTANCE);
            }
        }.perform(event);

    }
}
