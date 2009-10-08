package org.jboss.jsr299.tck.tests.implementation.initializer;

import javax.ejb.Local;

@Local
public interface LocalChicken
{
   public void firstBusinessMethod();
   
   public void cluck();
}
