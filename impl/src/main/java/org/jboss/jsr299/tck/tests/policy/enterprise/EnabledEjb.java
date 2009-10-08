package org.jboss.jsr299.tck.tests.policy.enterprise;

import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

@Stateless
@Alternative
class EnabledEjb implements EjbInterface
{
   public String hello() {
      return "Hello world!";
   }
}
