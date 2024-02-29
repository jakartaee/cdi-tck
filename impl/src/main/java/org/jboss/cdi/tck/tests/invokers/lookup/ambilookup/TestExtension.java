/*
 * Copyright 2024, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.invokers.lookup.ambilookup;

import java.util.Set;

import jakarta.enterprise.inject.build.compatible.spi.*;

import org.jboss.cdi.tck.tests.invokers.InvokerHolderExtensionBase;

public class TestExtension extends InvokerHolderExtensionBase implements BuildCompatibleExtension {
    @Registration(types = MyService.class)
    public void myServiceRegistration(BeanInfo bean, InvokerFactory invokers) {
        registerInvokers(bean, invokers, Set.of("hello"), builder -> builder.withArgumentLookup(0));
    }

    @Synthesis
    public void synthesis(SyntheticComponents syn) {
        synthesizeInvokerHolder(syn);
    }
}
