package org.jboss.jsr299.tck.tests.lookup.circular;

import javax.inject.Inject;

@SuppressWarnings("unused")
class Planet
{
   
   private Space space;
   
   @Inject
   public Planet(Space space)
   {
      this.space = space;
   }
   
}
