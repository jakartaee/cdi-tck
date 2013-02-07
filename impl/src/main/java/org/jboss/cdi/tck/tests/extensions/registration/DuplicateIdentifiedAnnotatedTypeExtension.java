/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.extensions.registration;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;

public class DuplicateIdentifiedAnnotatedTypeExtension {

    public void registerBeans(@Observes BeforeBeanDiscovery event, final BeanManager bm) {
        
        final String nonUniqueId = DuplicateIdentifiedAnnotatedTypeExtension.class.getName() + "someNonUniqueId";
        
        // add BeanClassToRegister and Beanie with the same id
        
        new AddForwardingAnnotatedTypeAction<BeanClassToRegister>() {
            @Override
            public String getId() {
                return nonUniqueId;
            }

            @Override
            public String getBaseId() {
                return DuplicateIdentifiedAnnotatedTypeExtension.class.getName();
            }

            @Override
            public AnnotatedType<BeanClassToRegister> delegate() {
                return bm.createAnnotatedType(BeanClassToRegister.class);
            }
        }.perform(event);
        
        new AddForwardingAnnotatedTypeAction<Beanie>() {
            @Override
            public String getId() {
                return nonUniqueId;
            }

            @Override
            public String getBaseId() {
                return DuplicateIdentifiedAnnotatedTypeExtension.class.getName();
            }

            @Override
            public AnnotatedType<Beanie> delegate() {
                return bm.createAnnotatedType(Beanie.class);
            }
        }.perform(event);
    }
}
