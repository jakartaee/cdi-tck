package org.jboss.cdi.tck.test.spi.sp;

import java.util.Random;

import jakarta.enterprise.inject.Produces;

public class SomeProducer {
    @Produces
    public String generateName() {
        return "SomeName" + new Random().nextLong();
    }
}
