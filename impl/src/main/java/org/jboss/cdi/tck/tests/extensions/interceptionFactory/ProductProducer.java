/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.interceptionFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InterceptionFactory;
import javax.inject.Inject;

@ApplicationScoped
public class ProductProducer {

    @Inject
    ContainerProduct containerProduct;

    @Produces
    @RequestScoped
    public Product createInterceptedProduct(InterceptionFactory<Product> interceptionFactory) {
        interceptionFactory.configure().add(ProductInterceptorBinding3.BindingLiteral.INSTANCE).filterMethods(m -> m.getJavaMember().getName().equals("ping"))
                .findFirst().get()
                .add(ProductInterceptorBinding1.BindingLiteral.INSTANCE)
                .add(ProductInterceptorBinding2.BindingLiteral.INSTANCE);
        return interceptionFactory.createInterceptedInstance(new Product());
    }

    @Produces
    @Custom
    public FinalProduct createInterceptedFinalProduct(InterceptionFactory<FinalProduct> interceptionFactory) {
        interceptionFactory.ignoreFinalMethods().configure().filterMethods(m -> m.getJavaMember().getName().equals("ping"))
                .findFirst().get()
                .add(ProductInterceptorBinding1.BindingLiteral.INSTANCE)
                .add(ProductInterceptorBinding2.BindingLiteral.INSTANCE);
        return interceptionFactory.createInterceptedInstance(new FinalProduct());
    }

    @Produces
    @Custom
    public ContainerProduct noInterceptionProducer (InterceptionFactory<ContainerProduct> interceptionFactory) {
        interceptionFactory.configure().filterMethods(m -> m.getJavaMember().getName().equals("ping"))
                .findFirst().get()
                .add(ProductInterceptorBinding1.BindingLiteral.INSTANCE);
        return interceptionFactory.createInterceptedInstance(containerProduct);
    }
}
