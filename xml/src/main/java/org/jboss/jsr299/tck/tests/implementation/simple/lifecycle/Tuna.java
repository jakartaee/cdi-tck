package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.context.RequestScoped;

@AnotherDeploymentType
@RequestScoped
class Tuna
{
   
   public String getName()
   {
      return "Ophir";
   }

}
