package org.jboss.jsr299.tck.tests.event.select;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

class SecuritySensor
{
   @Inject @Any Event<SecurityEvent> securityEvent;
}
