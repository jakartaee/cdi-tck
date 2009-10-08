package org.jboss.jsr299.tck.tests.lookup.injection;

import javax.inject.Inject;

class WolfPack
{
   @Inject Wolf alphaWolf;
   
   public Wolf getAlphaWolf()
   {
      return alphaWolf;
   }
}
