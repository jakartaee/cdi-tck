package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.BeanTypes;
import javax.enterprise.inject.Produces;

public class PetShop
{
   
   @Produces @BeanTypes(Dove.class)
   private Dove dove = new Dove("charlie");
   
   @Produces @BeanTypes(Parrot.class)
   public Parrot getParrot()
   {
      return new Parrot("polly");
   }

}
