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
package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InterceptionFactory;
import jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator;

@ApplicationScoped
public class ProducerBean {

    @Produces
    @ApplicationScoped
    public Product createProduct(InterceptionFactory<Product> interceptionFactory) {
        AnnotatedTypeConfigurator<Product> configurator = interceptionFactory.configure();
        configurator.add(ProductInterceptorBinding1.Literal.INSTANCE)
                .filterMethods(m -> m.getJavaMember().getName().equals("ping"))
                .findFirst().get()
                .add(ProductInterceptorBinding2.Literal.INSTANCE);
        configurator.filterMethods(m -> m.getJavaMember().getName().equals("pong"))
                .findFirst().get()
                .add(ProductInterceptorBinding3.Literal.INSTANCE);
        return interceptionFactory.createInterceptedInstance(new Product());
    }
}