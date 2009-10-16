package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class Tuna
{
   public String getName()
   {
      return "Ophir";
   }

}
