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
package org.jboss.cdi.tck.tests.full.decorators.custom.broken.nodelegateinjectionpoint;

import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

public class AfterBeanDiscoveryObserver implements Extension {

    private static CustomDecorator decorator;

    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {

        AnnotatedType<VehicleDecorator> type = beanManager.createAnnotatedType(VehicleDecorator.class);
        Set<AnnotatedField<? super VehicleDecorator>> annotatedFields = type.getFields();
        AnnotatedField<? super VehicleDecorator> annotatedField = annotatedFields.iterator().next();
        decorator = new CustomDecorator(annotatedField, beanManager);

        event.addBean(decorator);
    }

    public void vetoVehicleDecorator(@Observes ProcessAnnotatedType<VehicleDecorator> event) {
        event.veto();
    }

    public static CustomDecorator getDecorator() {
        return decorator;
    }
}
