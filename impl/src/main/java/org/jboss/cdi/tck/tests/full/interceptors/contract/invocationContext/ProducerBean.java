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
