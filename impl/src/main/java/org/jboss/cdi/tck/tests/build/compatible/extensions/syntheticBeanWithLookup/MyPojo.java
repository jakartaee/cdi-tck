package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanWithLookup;

import java.util.concurrent.atomic.AtomicInteger;

public class MyPojo {
    static final AtomicInteger createdCounter = new AtomicInteger(0);
    static final AtomicInteger destroyedCounter = new AtomicInteger(0);

    public MyPojo() {
        createdCounter.incrementAndGet();
    }

    public String hello() {
        return "Hello!";
    }

    public void destroy() {
        destroyedCounter.incrementAndGet();
    }
}
