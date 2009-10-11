package org.jboss.jsr299.tck.tests.extensions.processBean;

import javax.enterprise.inject.Produces;

public class ChickenHutch
{
   @Produces
   private Chicken chicken;

}
