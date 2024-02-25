/*
 * Copyright 2024, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.invokers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.InvokerInfo;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.invoke.InvokerBuilder;

public abstract class InvokerHolderExtensionBase {
    private final Map<String, InvokerInfo> invokers = new LinkedHashMap<>();

    protected final void registerInvokers(BeanInfo bean, InvokerFactory invokers, Set<String> methods) {
        registerInvokers(bean, invokers, methods, builder -> {
        });
    }

    protected final void registerInvokers(BeanInfo bean, InvokerFactory invokers, Set<String> methods,
            Consumer<InvokerBuilder<?>> action) {
        bean.declaringClass()
                .methods()
                .stream()
                .filter(it -> methods.contains(it.name()))
                .forEach(it -> {
                    InvokerBuilder<InvokerInfo> builder = invokers.createInvoker(bean, it);
                    action.accept(builder);
                    registerInvoker(it.name(), builder.build());
                });
    }

    protected final void registerInvoker(String id, InvokerInfo invoker) {
        invokers.put(id, invoker);
    }

    protected final void synthesizeInvokerHolder(SyntheticComponents syn) {
        syn.addBean(InvokerHolder.class)
                .type(InvokerHolder.class)
                .withParam("names", invokers.keySet().toArray(String[]::new))
                .withParam("invokers", invokers.values().toArray(InvokerInfo[]::new))
                .createWith(InvokerHolderCreator.class);
    }
}
