package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition;

class DefangedTarantula extends Tarantula
{
   private int deaths;
   
   public DefangedTarantula(int deaths)
   {
      this.deaths = deaths;
   }

   @Override public int getDeathsCaused() { return deaths; }
}
