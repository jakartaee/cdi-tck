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
package org.jboss.jsr299.tck.tests.event.implicit;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.util.TypeLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * Test that there is an implicit event bean for every legal event type
 * and verify its composition.
 * 
 * @author Dan Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class ImplicitEventTest extends AbstractJSR299Test
{
   private static final TypeLiteral<Event<StudentRegisteredEvent>> STUDENT_REGISTERED_EVENT_LITERAL = new TypeLiteral<Event<StudentRegisteredEvent>>() {};
   private static final TypeLiteral<Event<CourseFullEvent>> COURSE_FULL_EVENT_LITERAL = new TypeLiteral<Event<CourseFullEvent>>() {};
   private static final TypeLiteral<Event<AwardEvent>> AWARD_EVENT_LITERAL = new TypeLiteral<Event<AwardEvent>>() {};
   
   @Test(groups = "events")
   @SpecAssertion(section = "10.3.2", id = "a")
   public void testImplicitEventExistsForEachEventType()
   {
      assert getBeans(STUDENT_REGISTERED_EVENT_LITERAL).size() == 1;
      assert getBeans(COURSE_FULL_EVENT_LITERAL).size() == 1;
      assert getBeans(AWARD_EVENT_LITERAL).size() == 1;
   }
   
   @Test(groups = "events")
   @SpecAssertion(section = "10.3.2", id = "b")
   public void testImplicitEventHasAllExplicitBindingTypes()
   {
      assert getBeans(AWARD_EVENT_LITERAL, new AnyLiteral(), new HonorsLiteral()).size() == 1;
   }
   
   @SpecAssertion(section = "10.1", id = "i")
   public void testImplicitEventHasAnyBinding()
   {
      assert getUniqueBean(STUDENT_REGISTERED_EVENT_LITERAL).getQualifiers().contains(new AnyLiteral());
      assert getUniqueBean(COURSE_FULL_EVENT_LITERAL).getQualifiers().contains(new AnyLiteral());
      assert getUniqueBean(AWARD_EVENT_LITERAL).getQualifiers().contains(new AnyLiteral());
      assert getUniqueBean(AWARD_EVENT_LITERAL, new HonorsLiteral()).getQualifiers().contains(new AnyLiteral());
      assert getUniqueBean(AWARD_EVENT_LITERAL, new AnyLiteral(), new HonorsLiteral()) == getUniqueBean(AWARD_EVENT_LITERAL, new HonorsLiteral());
   }
   
   @Test(groups = "events")
   @SpecAssertion(section = "10.3.2", id = "d")
   public void testImplicitEventHasDependentScope()
   {
      assert getUniqueBean(STUDENT_REGISTERED_EVENT_LITERAL).getScope().equals(Dependent.class);
   }
   
   @Test(groups = "events")
   @SpecAssertion(section = "10.3.2", id = "e")
   public void testImplicitEventHasNoName()
   {
      assert getUniqueBean(STUDENT_REGISTERED_EVENT_LITERAL).getName() == null;
   }
   
   @Test(groups = "events")
   @SpecAssertion(section = "10.3.2", id = "f")
   public void testImplicitEventHasImplementation()
   {
      StudentDirectory directory = getInstanceByType(StudentDirectory.class);
      directory.reset();
      Registration registration = getInstanceByType(Registration.class);
      assert registration.getInjectedCourseFullEvent() != null;
      assert registration.getInjectedStudentRegisteredEvent() != null;
      Event<StudentRegisteredEvent> event = registration.getInjectedStudentRegisteredEvent();
      Student student = new Student("Dan");
      event.fire(new StudentRegisteredEvent(student));
      assert directory.getStudents().size() == 1;
      assert directory.getStudents().contains(student);
   }
   
   @Test(groups = { "events" })
   @SpecAssertions({
      @SpecAssertion(section = "10.3.2", id = "g"),
      @SpecAssertion(section = "6.6.2", id = "e") // TODO break up this assertion into smaller bits
   })
   public void testImplicitEventIsPassivationCapable() throws IOException, ClassNotFoundException
   {
      StudentDirectory directory = getInstanceByType(StudentDirectory.class);
      directory.reset();
      Registration registration = getInstanceByType(Registration.class);
      Event<StudentRegisteredEvent> event = registration.getInjectedStudentRegisteredEvent();
      assert Serializable.class.isAssignableFrom(event.getClass());
      byte[] serializedEvent = serialize(event);
      @SuppressWarnings("unchecked")
      Event<StudentRegisteredEvent> eventCopy = (Event<StudentRegisteredEvent>) deserialize(serializedEvent);
      // make sure we can still use it
      Student student = new Student("Dan");
      eventCopy.fire(new StudentRegisteredEvent(student));
      assert directory.getStudents().size() == 1;
      assert directory.getStudents().contains(student);
   }
   
}
