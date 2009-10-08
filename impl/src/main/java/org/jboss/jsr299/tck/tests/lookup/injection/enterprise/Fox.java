package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.enterprise.context.Dependent;

@Dependent
public class Fox
{
   
   public String getName()
   {
      return "gavin";
   }
   
}
