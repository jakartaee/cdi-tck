package org.jboss.jsr299.tck.tests.implementation.simple.resource.ejb;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

class ResourceProducer
{
   private @Produces @EJB @Lazy BeanRemote remoteEjb;
}
