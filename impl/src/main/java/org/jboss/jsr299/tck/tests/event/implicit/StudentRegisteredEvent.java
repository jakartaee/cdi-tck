package org.jboss.jsr299.tck.tests.event.implicit;

class StudentRegisteredEvent
{
   private Student student;
   
   public StudentRegisteredEvent(Student student)
   {
      this.student = student;
   }
   
   public Student getStudent()
   {
      return student;
   }
}
