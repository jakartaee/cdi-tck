package org.jboss.jsr299.tck.tests.event.broken.inject.withoutType;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

class BlackRumpedWaxbill_Broken
{
   @SuppressWarnings({"unchecked", "unused"})
   @Inject @Any 
   private Event simpleEvent;
}
