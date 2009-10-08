package org.jboss.jsr299.tck.tests.context.application.ejb;

import javax.ejb.Local;

@Local
public interface FeederService
{
   public void refillFood();
   public boolean adequateFood();
}
