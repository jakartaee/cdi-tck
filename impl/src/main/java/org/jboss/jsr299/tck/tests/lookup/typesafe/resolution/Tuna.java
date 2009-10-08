package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class Tuna
{
   
   public String getName()
   {
      return "Ophir";
   }

}
