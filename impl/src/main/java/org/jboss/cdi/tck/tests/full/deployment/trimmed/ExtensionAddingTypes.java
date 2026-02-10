/*
 * Copyright 2026, Contributors to the Eclipse Foundation
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

package org.jboss.cdi.tck.tests.full.deployment.trimmed;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterTypeDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

public class ExtensionAddingTypes implements Extension {

    public void afterTypeDiscovery(@Observes AfterTypeDiscovery event, BeanManager beanManager) {
        event.addAnnotatedType(beanManager.createAnnotatedType(UnannotatedTypeOne.class),
                UnannotatedTypeOne.class.getName() + ExtensionAddingTypes.class.getName());
        event.addAnnotatedType(UnannotatedTypeThree.class,
                UnannotatedTypeThree.class.getName() + ExtensionAddingTypes.class.getName())
                .add(ApplicationScoped.Literal.INSTANCE);
    }

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) {
        event.addAnnotatedType(beanManager.createAnnotatedType(UnannotatedTypeTwo.class),
                UnannotatedTypeTwo.class.getName() + ExtensionAddingTypes.class.getName());
        event.addAnnotatedType(UnannotatedTypeFour.class,
                UnannotatedTypeFour.class.getName() + ExtensionAddingTypes.class.getName())
                .add(ApplicationScoped.Literal.INSTANCE);
    }
}
