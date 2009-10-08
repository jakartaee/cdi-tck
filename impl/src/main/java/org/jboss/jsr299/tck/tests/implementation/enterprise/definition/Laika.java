package org.jboss.jsr299.tck.tests.implementation.enterprise.definition;

import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

@Singleton
@ApplicationScoped
class Laika
{

   @Remove
   public void remove()
   {
      
   }
   
}
