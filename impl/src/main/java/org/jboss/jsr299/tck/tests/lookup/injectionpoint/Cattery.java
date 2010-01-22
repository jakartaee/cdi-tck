package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import javax.inject.Inject;

public class Cattery
{
   
   @Inject Cat cat;
   
   public Cat getCat()
   {
      return cat;
   }

}
