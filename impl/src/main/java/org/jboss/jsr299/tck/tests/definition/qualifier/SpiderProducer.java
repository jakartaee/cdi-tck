package org.jboss.jsr299.tck.tests.definition.qualifier;

import javax.enterprise.inject.Produces;

class SpiderProducer
{
   
   @Produces @Tame public Tarantula produceTameTarantula()
   {
      return new DefangedTarantula();
   }
   
   @Produces @Produced public Spider produceSpiderFromInjection(@Tame Tarantula tarantula) 
   {
      return tarantula;
   }

}