/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.select;

import static org.jboss.cdi.tck.cdi.Sections.EVENT;

import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
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
@SpecVersion(spec = "cdi", version = "2.0")
public class SelectEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SelectEventTest.class).build();
    }

    @Test
    @SpecAssertion(section = EVENT, id = "eaa")
    public void testEventSelectReturnsEventOfSameType() {
        AlarmSystem alarm = getContextualReference(AlarmSystem.class);
        alarm.reset();
        SecuritySensor sensor = getContextualReference(SecuritySensor.class);

        sensor.securityEvent.fire(new SecurityEvent());
        assert alarm.getNumSecurityEvents() == 1;
        assert alarm.getNumSystemTests() == 0;
        assert alarm.getNumBreakIns() == 0;
        assert alarm.getNumViolentBreakIns() == 0;

        sensor.securityEvent.select(new SystemTest.SystemTestLiteral("") {
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

        sensor.securityEvent.select(BreakInEvent.class, new Violent.Literal()).fire(new BreakInEvent());
        assert alarm.getNumSecurityEvents() == 4;
        assert alarm.getNumSystemTests() == 1;
        assert alarm.getNumBreakIns() == 2;
        assert alarm.getNumViolentBreakIns() == 1;
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = EVENT, id = "eab")
    public <T> void testEventSelectThrowsExceptionIfEventTypeHasTypeVariable() {
        SecuritySensor sensor = getContextualReference(SecuritySensor.class);
        sensor.securityEvent.select(new TypeLiteral<SecurityEvent_Illegal<T>>() {
        });
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = EVENT, id = "eba")
    public void testEventSelectThrowsExceptionForDuplicateBindingType() {
        SecuritySensor sensor = getContextualReference(SecuritySensor.class);
        sensor.securityEvent.select(new SystemTest.SystemTestLiteral("a") {
        }, new SystemTest.SystemTestLiteral("b") {
        });
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = EVENT, id = "eba")
    public void testEventSelectWithSubtypeThrowsExceptionForDuplicateBindingType() {
        SecuritySensor sensor = getContextualReference(SecuritySensor.class);
        sensor.securityEvent.select(BreakInEvent.class, new SystemTest.SystemTestLiteral("a") {
        }, new SystemTest.SystemTestLiteral("b") {
        });
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = EVENT, id = "ec")
    public void testEventSelectThrowsExceptionIfAnnotationIsNotBindingType() {
        SecuritySensor sensor = getContextualReference(SecuritySensor.class);
        sensor.securityEvent.select(new NotABindingType.Literal());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = EVENT, id = "ec")
    public void testEventSelectWithSubtypeThrowsExceptionIfAnnotationIsNotBindingType() {
        SecuritySensor sensor = getContextualReference(SecuritySensor.class);
        sensor.securityEvent.select(BreakInEvent.class, new NotABindingType.Literal());
    }
}
