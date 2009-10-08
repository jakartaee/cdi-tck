package org.jboss.jsr299.tck.tests.extensions.beanManager;

import javax.inject.Inject;

class DogHouse
{
   @Inject
   private Terrier dog;

   public Terrier getDog()
   {
      return dog;
   }
}
