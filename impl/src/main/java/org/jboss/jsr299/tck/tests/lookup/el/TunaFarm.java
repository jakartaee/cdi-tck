package org.jboss.jsr299.tck.tests.lookup.el;

import javax.inject.Inject;
import javax.inject.Named;

@Named
class TunaFarm
{

   @SuppressWarnings("unused")
   @Inject
   public Tuna tuna;
   
   public Tuna notInjectedTuna = new Tuna();

   
}
