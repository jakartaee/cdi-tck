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
package org.jboss.cdi.tck.tests.event.implicit;

import static org.jboss.cdi.tck.cdi.Sections.BUILTIN_EVENT;
import static org.jboss.cdi.tck.cdi.Sections.EVENT_TYPES_AND_QUALIFIER_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that there is an implicit event bean for every legal event type and verify its composition.
 * 
 * @author Dan Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ImplicitEventTest extends AbstractTest {
    private static final TypeLiteral<Event<StudentRegisteredEvent>> STUDENT_REGISTERED_EVENT_LITERAL = new TypeLiteral<Event<StudentRegisteredEvent>>() {
    };
    private static final TypeLiteral<Event<CourseFullEvent>> COURSE_FULL_EVENT_LITERAL = new TypeLiteral<Event<CourseFullEvent>>() {
    };
    private static final TypeLiteral<Event<AwardEvent>> AWARD_EVENT_LITERAL = new TypeLiteral<Event<AwardEvent>>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ImplicitEventTest.class).build();
    }

    @Test
    @SpecAssertion(section = BUILTIN_EVENT, id = "a")
    public void testImplicitEventExistsForEachEventType() {
        assert getBeans(STUDENT_REGISTERED_EVENT_LITERAL).size() == 1;
        assert getBeans(COURSE_FULL_EVENT_LITERAL).size() == 1;
        assert getBeans(AWARD_EVENT_LITERAL).size() == 1;
    }

    @Test
    @SpecAssertion(section = BUILTIN_EVENT, id = "b")
    public void testImplicitEventHasAllExplicitBindingTypes() {
        assert getBeans(AWARD_EVENT_LITERAL, AnyLiteral.INSTANCE, new HonorsLiteral()).size() == 1;
    }

    @SpecAssertion(section = EVENT_TYPES_AND_QUALIFIER_TYPES, id = "i")
    public void testImplicitEventHasAnyBinding() {
        assert getUniqueBean(STUDENT_REGISTERED_EVENT_LITERAL).getQualifiers().contains(AnyLiteral.INSTANCE);
        assert getUniqueBean(COURSE_FULL_EVENT_LITERAL).getQualifiers().contains(AnyLiteral.INSTANCE);
        assert getUniqueBean(AWARD_EVENT_LITERAL).getQualifiers().contains(AnyLiteral.INSTANCE);
        assert getUniqueBean(AWARD_EVENT_LITERAL, new HonorsLiteral()).getQualifiers().contains(AnyLiteral.INSTANCE);
        assert getUniqueBean(AWARD_EVENT_LITERAL, AnyLiteral.INSTANCE, new HonorsLiteral()) == getUniqueBean(
                AWARD_EVENT_LITERAL, new HonorsLiteral());
    }

    @Test
    @SpecAssertion(section = BUILTIN_EVENT, id = "d")
    public void testImplicitEventHasDependentScope() {
        assert getUniqueBean(STUDENT_REGISTERED_EVENT_LITERAL).getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = BUILTIN_EVENT, id = "e")
    public void testImplicitEventHasNoName() {
        assert getUniqueBean(STUDENT_REGISTERED_EVENT_LITERAL).getName() == null;
    }

    @Test
    @SpecAssertion(section = BUILTIN_EVENT, id = "f")
    public void testImplicitEventHasImplementation() {
        StudentDirectory directory = getContextualReference(StudentDirectory.class);
        directory.reset();
        Registration registration = getContextualReference(Registration.class);
        assert registration.getInjectedCourseFullEvent() != null;
        assert registration.getInjectedStudentRegisteredEvent() != null;
        Event<StudentRegisteredEvent> event = registration.getInjectedStudentRegisteredEvent();
        Student student = new Student("Dan");
        event.fire(new StudentRegisteredEvent(student));
        assert directory.getStudents().size() == 1;
        assert directory.getStudents().contains(student);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BUILTIN_EVENT, id = "g"),
            @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "eb") })
    public void testImplicitEventIsPassivationCapable() throws IOException, ClassNotFoundException {
        StudentDirectory directory = getContextualReference(StudentDirectory.class);
        directory.reset();
        Registration registration = getContextualReference(Registration.class);
        Event<StudentRegisteredEvent> event = registration.getInjectedStudentRegisteredEvent();
        assert Serializable.class.isAssignableFrom(event.getClass());
        byte[] serializedEvent = passivate(event);
        @SuppressWarnings("unchecked")
        Event<StudentRegisteredEvent> eventCopy = (Event<StudentRegisteredEvent>) activate(serializedEvent);
        // make sure we can still use it
        Student student = new Student("Dan");
        eventCopy.fire(new StudentRegisteredEvent(student));
        assert directory.getStudents().size() == 1;
        assert directory.getStudents().contains(student);
    }

}
