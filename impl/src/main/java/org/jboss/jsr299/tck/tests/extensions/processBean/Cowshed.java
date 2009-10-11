package org.jboss.jsr299.tck.tests.extensions.processBean;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

public class Cowshed
{
   @Produces
   public Cow getDaisy()
   {
      return new Cow("Daisy");
   }
   
   public void disposeOfDaisy(@Disposes Cow daisy)
   {
      
   }

}
