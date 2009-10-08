package org.jboss.jsr299.tck.tests.context.passivating.broken.decoratorWithNonPassivatingInitializerMethod;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class UnderwaterCity implements CityInterface, Serializable
{

   public void foo()
   {
      
   }

}
