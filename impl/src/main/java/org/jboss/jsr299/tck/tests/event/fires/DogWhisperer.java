package org.jboss.jsr299.tck.tests.event.fires;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

class DogWhisperer
{
   @Inject @Any @Tame @Role("Master") 
   Event<TamingCommand> tamingEvent;
   
   @Inject @Any
   Event<Praise> praiseEvent;
   
   public void issueTamingCommand()
   {
      tamingEvent.fire(new TamingCommand());
   }
   
   public void givePraise()
   {
      praiseEvent.fire(new Praise());
   }
}
