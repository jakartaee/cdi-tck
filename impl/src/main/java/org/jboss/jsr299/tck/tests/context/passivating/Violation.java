package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.inject.Alternative;

@Alternative
class Violation
{
   public void ping()
   {
   }
}
