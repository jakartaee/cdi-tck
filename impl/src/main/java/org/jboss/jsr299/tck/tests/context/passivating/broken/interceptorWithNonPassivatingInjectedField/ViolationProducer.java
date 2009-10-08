package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingInjectedField;

import javax.enterprise.inject.Produces;

class ViolationProducer
{
   public final @Produces Violation getViolation() { return new Violation(null); } 
}
