package org.jboss.jsr299.tck.tests.event.observer.wildcardAndTypeVariable;

import javax.enterprise.event.Observes;

class BostonTerrier
{
   
   public static boolean observed;
   
   public <T extends Behavior> void observesEventWithTypeParameter(@Observes T behavior)
   {
      observed = true;
   }
}
