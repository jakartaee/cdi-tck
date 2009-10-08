package org.jboss.jsr299.tck.tests.implementation.producer.method.broken.enterprise.nonbusiness;

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;

@Stateful
public class FooProducer implements FooProducerLocal
{
   @Produces Foo createFoo() { return new Foo(); }
}
