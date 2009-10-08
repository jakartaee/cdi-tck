package org.jboss.jsr299.tck.tests.definition.qualifier;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class Tuna
{
   
   public String getName()
   {
      return "Ophir";
   }

}
