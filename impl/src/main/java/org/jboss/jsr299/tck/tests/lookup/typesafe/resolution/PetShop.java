package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;

public class PetShop
{
   
   @Produces @Typed(Dove.class)
   private Dove dove = new Dove("charlie");
   
   @Produces @Typed(Parrot.class)
   public Parrot getParrot()
   {
      return new Parrot("polly");
   }
   
   @Produces @Typed(Cat.class) @Tame
   private DomesticCat felix = new DomesticCat("felix");
   
   @Produces @Typed(Cat.class) @Wild
   public Lion getAslan()
   {
      return new Lion("timmy");
   }

}
