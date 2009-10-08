package org.jboss.jsr299.tck.tests.implementation.enterprise.definition;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;

@Stateful
@ApplicationScoped
@Named @Default
class Pitbull extends Dog implements PitbullLocal, DogLocal, DogRemote
{
   
   public static boolean destructorCalled = false;

   @Remove
   public void bye() 
   {
      destructorCalled = true;
   }
}
