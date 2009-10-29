package org.jboss.jsr299.tck.tests.event.select;

import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * These tests verify the behavior of the Event#select() method.
 * 
 * @author Dan Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class SelectEventTest extends AbstractJSR299Test
{
   @Test(groups = "events")
   @SpecAssertion(section = "10.3.1", id = "eaa")
   public void testEventSelectReturnsEventOfSameType()
   {
      AlarmSystem alarm = getInstanceByType(AlarmSystem.class);
      alarm.reset();
      SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
      
      sensor.securityEvent.fire(new SecurityEvent());
      assert alarm.getNumSecurityEvents() == 1;
      assert alarm.getNumSystemTests() == 0;
      assert alarm.getNumBreakIns() == 0;
      assert alarm.getNumViolentBreakIns() == 0;
      
      sensor.securityEvent.select(new AnnotationLiteral<SystemTest>() {}).fire(new SecurityEvent());
      assert alarm.getNumSecurityEvents() == 2;
      assert alarm.getNumSystemTests() == 1;
      assert alarm.getNumBreakIns() == 0;
      assert alarm.getNumViolentBreakIns() == 0;
      
      sensor.securityEvent.select(BreakInEvent.class).fire(new BreakInEvent());
      assert alarm.getNumSecurityEvents() == 3;
      assert alarm.getNumSystemTests() == 1;
      assert alarm.getNumBreakIns() == 1;
      assert alarm.getNumViolentBreakIns() == 0;
      
      sensor.securityEvent.select(BreakInEvent.class, new AnnotationLiteral<Violent>() {}).fire(new BreakInEvent());
      assert alarm.getNumSecurityEvents() == 4;
      assert alarm.getNumSystemTests() == 1;
      assert alarm.getNumBreakIns() == 2;
      assert alarm.getNumViolentBreakIns() == 1;
   }
   
   @Test(groups = {"events" }, expectedExceptions = IllegalArgumentException.class)
   @SpecAssertion(section = "10.3.1", id = "eab")
   public <T> void testEventSelectThrowsExceptionIfEventTypeHasTypeVariable()
   {
      SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
      sensor.securityEvent.select(new TypeLiteral<SecurityEvent_Illegal<T>>() {});
   }
   
   @Test(groups = "events", expectedExceptions = IllegalArgumentException.class)
   @SpecAssertion(section = "10.3.1", id = "eba")
   public void testEventSelectThrowsExceptionForDuplicateBindingType()
   {
      SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
      sensor.securityEvent.select(new AnnotationLiteral<SystemTest>() {}, new AnnotationLiteral<SystemTest>() {});
   }
   
   @Test(groups = "events", expectedExceptions = IllegalArgumentException.class)
   @SpecAssertion(section = "10.3.1", id = "eba")
   public void testEventSelectWithSubtypeThrowsExceptionForDuplicateBindingType()
   {
      SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
      sensor.securityEvent.select(BreakInEvent.class, new AnnotationLiteral<Violent>() {}, new AnnotationLiteral<Violent>() {});
   }
   
   @Test(groups = "events", expectedExceptions = IllegalArgumentException.class)
   @SpecAssertion(section = "10.3.1", id = "ec")
   public void testEventSelectThrowsExceptionIfAnnotationIsNotBindingType()
   {
      SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
      sensor.securityEvent.select(new AnnotationLiteral<NotABindingType>() {});
   }
   
   @Test(groups = "events", expectedExceptions = IllegalArgumentException.class)
   @SpecAssertion(section = "10.3.1", id = "ec")
   public void testEventSelectWithSubtypeThrowsExceptionIfAnnotationIsNotBindingType()
   {
      SecuritySensor sensor = getInstanceByType(SecuritySensor.class);
      sensor.securityEvent.select(BreakInEvent.class, new AnnotationLiteral<NotABindingType>() {});
   }
}
