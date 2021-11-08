package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanWithLookup;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;

import java.util.concurrent.atomic.AtomicInteger;

@Dependent
public class MyDependentBean {
    static final AtomicInteger createdCounter = new AtomicInteger(0);
    static final AtomicInteger destroyedCounter = new AtomicInteger(0);

    @PostConstruct
    void postConstruct() {
        createdCounter.incrementAndGet();
    }

    @PreDestroy
    void preDestroy() {
        destroyedCounter.incrementAndGet();
    }
}
