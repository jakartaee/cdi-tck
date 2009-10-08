package org.jboss.jsr299.tck.tests.implementation.simple.resource.resource;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;

class ResourceProducer
{
   @Produces @Another 
   @Resource(mappedName = "java:app/BeanManager") 
   private BeanManager manager;
}
