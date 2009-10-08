package org.jboss.jsr299.tck.tests.event.implicit;

class CourseFullEvent
{
   private Course course;

   public CourseFullEvent(Course course)
   {
      this.course = course;
   }
   
   public Course getCourse()
   {
      return course;
   }
}
