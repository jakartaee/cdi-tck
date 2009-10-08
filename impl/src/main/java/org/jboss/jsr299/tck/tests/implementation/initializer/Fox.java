package org.jboss.jsr299.tck.tests.implementation.initializer;

import javax.enterprise.context.Dependent;

@Dependent
class Fox
{
   
   public String getName()
   {
      return "gavin";
   }
   
}
