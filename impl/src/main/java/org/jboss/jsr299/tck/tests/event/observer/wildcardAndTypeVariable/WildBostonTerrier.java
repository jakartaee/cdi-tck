package org.jboss.jsr299.tck.tests.event.observer.wildcardAndTypeVariable;

import java.util.List;

import javax.enterprise.event.Observes;

class WildBostonTerrier
{
   
   public static boolean observed;
   
   public void observesEventTypeWithWildcard(@Observes List<?> someArray)
   {
      observed = true;
   }
}
