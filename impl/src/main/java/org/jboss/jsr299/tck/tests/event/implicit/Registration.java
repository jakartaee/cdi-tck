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
