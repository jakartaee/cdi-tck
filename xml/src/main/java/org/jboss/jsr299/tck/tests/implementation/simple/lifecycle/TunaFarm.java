package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.inject.Current;
import javax.inject.Production;

@Production
class TunaFarm
{

   @SuppressWarnings("unused")
   @Current
   public Tuna tuna;
   
   public Tuna notInjectedTuna = new Tuna();

   
}
