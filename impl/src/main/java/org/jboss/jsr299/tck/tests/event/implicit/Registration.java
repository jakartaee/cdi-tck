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

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

class Registration
{
   @Inject @Any private Event<StudentRegisteredEvent> studentRegisteredEvent;
   
   private Event<CourseFullEvent> courseFullEvent;
   
   @Inject 
   public Registration(Event<CourseFullEvent> courseFullEvent)
   {
      this.courseFullEvent = courseFullEvent;
   }
   
   public void registerStudent(Student student)
   {
      studentRegisteredEvent.fire(new StudentRegisteredEvent(student));
   }
   
   public void registerForCourse(Course course, Student user)
   {
      courseFullEvent.fire(new CourseFullEvent(course));
   }
   
   Event<StudentRegisteredEvent> getInjectedStudentRegisteredEvent()
   {
      return studentRegisteredEvent;
   }
   
   Event<CourseFullEvent> getInjectedCourseFullEvent()
   {
      return courseFullEvent;
   }
}
