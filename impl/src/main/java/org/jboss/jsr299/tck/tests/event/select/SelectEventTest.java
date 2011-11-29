/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.tests.event.select;

import static org.jboss.jsr299.tck.TestGroups.EVENTS;

import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * These tests verify the behavior of the Event#select() method.
 * 
 * @author Dan Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class SelectEventTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SelectEventTest.class).build();
    }

    @Test(groups = EVENTS)
    @SpecAssertion(section = "10.3.1", id = "eaa")
    public void testEventSelectReturnsEventOfSameType() {
        AlarmSystem alarm = getInstanceByType(AlarmSystem.class);
        alarm.reset();
        SecuritySensor sensor = getInstanceByType(SecuritySensor.class);

        sensor.securityEvent.fire(new SecurityEvent());
        assert alarm.getNumSecurityEvents() == 1;
        assert alarm.getNumSystemTests() == 0;
        assert alarm.getNumBreakIns() == 0;
        assert alarm.getNumViolentBreakIns() == 0;

        sensor.securityEvent.select(new AnnotationLiteral<SystemTest>() {
        }).fire(new SecurityEvent());
        assert alarm.getNumSecurityEvents() == 2;
        assert alarm.getNumSystemTests() == 1;
        assert alarm.getNumBreakIns() == 0;
        assert alarm.getNumViolentBreakIns() == 0;

        sensor.securityEvent.select(BreakInEvent.class).fire(new BreakInEvent());
        assert alarm.getNumSecurityEvents() == 3;
        assert alarm.getNumSystemTests() == 1;
        assert alarm.getNumBreakIns() == 1;
        assert alarm.getNumViolentBreakIns() == 0;

        sensor.securityEvent.select(BreakInEvent.class, new AnnotationLiteral<Violent>() {
        }).fire(new BreakInEvent());
        assert alarm.getNumSecurityEvents() == 4;
        assert alarm.getNumSystemTests() == 1;
        assert alarm.getNumBreakIns() == 2;
        assert alarm.getNumViolentBreakIns() == 1;
    }

    @Test(groups = { EVENTS }, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "10.3.1", id = "eab")
    public <T> void testEventSelectThrowsExceptionIfEventTypeHasTypeVariable() {
        SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
        sensor.securityEvent.select(new TypeLiteral<SecurityEvent_Illegal<T>>() {
        });
    }

    @Test(groups = EVENTS, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "10.3.1", id = "eba")
    public void testEventSelectThrowsExceptionForDuplicateBindingType() {
        SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
        sensor.securityEvent.select(new AnnotationLiteral<SystemTest>() {
        }, new AnnotationLiteral<SystemTest>() {
        });
    }

    @Test(groups = EVENTS, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "10.3.1", id = "eba")
    public void testEventSelectWithSubtypeThrowsExceptionForDuplicateBindingType() {
        SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
        sensor.securityEvent.select(BreakInEvent.class, new AnnotationLiteral<Violent>() {
        }, new AnnotationLiteral<Violent>() {
        });
    }

    @Test(groups = EVENTS, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "10.3.1", id = "ec")
    public void testEventSelectThrowsExceptionIfAnnotationIsNotBindingType() {
        SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
        sensor.securityEvent.select(new AnnotationLiteral<NotABindingType>() {
        });
    }

    @Test(groups = EVENTS, expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "10.3.1", id = "ec")
    public void testEventSelectWithSubtypeThrowsExceptionIfAnnotationIsNotBindingType() {
        SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
        sensor.securityEvent.select(BreakInEvent.class, new AnnotationLiteral<NotABindingType>() {
        });
    }
}
