package org.jboss.jsr299.tck.tests.event.observer.enterprise;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

public @Local interface FarmerLocal
{
   void observeEggLaying(@Observes Egg egg);
}
