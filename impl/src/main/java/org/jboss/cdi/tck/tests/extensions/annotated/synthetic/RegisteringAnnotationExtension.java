/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.annotated.synthetic;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;

public class RegisteringAnnotationExtension implements Extension {

    void registerMy(@Observes BeforeBeanDiscovery event, final BeanManager manager) {

        new AddForwardingAnnotatedTypeAction<TestAnnotation>() {

            @Override
            public String getBaseId() {
                return RegisteringAnnotationExtension.class.getName();
            }

            @Override
            public AnnotatedType<TestAnnotation> delegate() {
                return manager.createAnnotatedType(TestAnnotation.class);
            }
        }.perform(event);

    }
    
    void registerJuicy(@Observes BeforeBeanDiscovery event, final BeanManager manager) {

        new AddForwardingAnnotatedTypeAction<Juicy>() {

            @Override
            public String getBaseId() {
                return RegisteringAnnotationExtension.class.getName();
            }

            @Override
            public AnnotatedType<Juicy> delegate() {
                return manager.createAnnotatedType(Juicy.class);
            }
        }.perform(event);

    }

}
