package org.jboss.jsr299.tck.tests.event.implicit;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;

class Awards
{
   private @Any @Honors Event<AwardEvent> honorsAwardEvent;
   
   public void grantHonorsStatus(Student student)
   {
      honorsAwardEvent.fire(new AwardEvent(student));
   }
}
