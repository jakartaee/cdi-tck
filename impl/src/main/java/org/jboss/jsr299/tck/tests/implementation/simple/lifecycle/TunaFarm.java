package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.inject.Inject;

class TunaFarm
{

   @SuppressWarnings("unused")
   @Inject
   public Tuna tuna;
   
   
   @SuppressWarnings("unused")
   @Inject @Tame
   public Tuna qualifiedTuna;
   
   public Tuna notInjectedTuna = new Tuna();
   
}
