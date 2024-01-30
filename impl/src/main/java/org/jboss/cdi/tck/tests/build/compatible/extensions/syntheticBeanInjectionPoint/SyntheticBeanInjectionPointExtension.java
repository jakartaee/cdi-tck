/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionPoint;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class SyntheticBeanInjectionPointExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn) {
        syn.addBean(MyDependentBean.class)
                .type(MyDependentBean.class)
                .scope(Dependent.class)
                .createWith(MyDependentBeanCreator.class)
                .disposeWith(MyDependentBeanDisposer.class);

        syn.addBean(MyApplicationScopedBean.class)
                .type(MyApplicationScopedBean.class)
                .scope(ApplicationScoped.class)
                .createWith(MyApplicationScopedBeanCreator.class);
    }
}
