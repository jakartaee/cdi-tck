package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.inject.Inject;

class TunaFarm
{

   @SuppressWarnings("unused")
   @Inject
   public Tuna tuna;
   
   public Tuna notInjectedTuna = new Tuna();

   
}
