package org.jboss.jsr299.tck.tests.event.observer.enterprise;

import javax.ejb.Stateful;
import javax.enterprise.event.Observes;

public @Stateful class Farmer implements FarmerLocal
{
   public void observeEggLaying(@Observes Egg egg)
   {
      egg.recordVisit(this);
   }

}