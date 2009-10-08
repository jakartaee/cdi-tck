package org.jboss.jsr299.tck.tests.event;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;


class OrangeCheekedWaxbill
{
   @Any @Role("Admin") @Tame
   protected Event<String> simpleEvent;

}
