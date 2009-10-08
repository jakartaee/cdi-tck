package org.jboss.jsr299.tck.tests.lookup.injection;

import javax.enterprise.context.Dependent;

@Dependent
class Fox
{
   
   public String getName()
   {
      return "gavin";
   }
   
}
