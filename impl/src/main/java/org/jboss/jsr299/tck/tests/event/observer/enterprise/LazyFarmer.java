package org.jboss.jsr299.tck.tests.event.observer.enterprise;

import javax.ejb.Stateful;

public @Stateful class LazyFarmer extends Farmer implements LazyFarmerLocal
{
}