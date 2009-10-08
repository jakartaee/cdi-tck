package org.jboss.jsr299.tck.tests.event.fires;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class DoggiePoints
{
   private int numPraiseReceived;
   
   private int numTamed;
   
   public void praiseReceived(@Observes @Any Praise praise)
   {
      numPraiseReceived++;
   }
   
   public void tamed(@Observes @Tame @Role("Master") TamingCommand tamed)
   {
      numTamed++;
   }
   
   public int getNumPraiseReceived()
   {
      return numPraiseReceived;
   }
   
   public int getNumTamed()
   {
      return numTamed;
   }
   
   public void reset()
   {
      numPraiseReceived = 0;
      numTamed = 0;
   }
}
