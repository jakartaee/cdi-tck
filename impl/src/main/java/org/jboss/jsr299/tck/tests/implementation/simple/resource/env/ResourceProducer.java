package org.jboss.jsr299.tck.tests.implementation.simple.resource.env;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;

class ResourceProducer
{
   @Produces @Greeting
   @Resource(name = "greeting")
   private String greeting;
}
