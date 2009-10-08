package org.jboss.jsr299.tck.tests.event.implicit;

class AwardEvent
{
   private Student student;

   public AwardEvent(Student student)
   {
      this.student = student;
   }
   
   public Student getStudent()
   {
      return student;
   }
}
