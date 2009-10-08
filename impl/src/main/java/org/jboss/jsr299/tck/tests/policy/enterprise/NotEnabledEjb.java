package org.jboss.jsr299.tck.tests.policy.enterprise;

import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

@Stateless
@Alternative
class NotEnabledEjb implements DisabledEjbInterface
{
   public String hello() {
      return "Hi!";
   }
}
