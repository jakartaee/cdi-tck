package org.jboss.jsr299.tck.tests.event.implicit;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class StudentDirectory
{
   private Set<Student> students = new HashSet<Student>();
   
   public void addStudent(@Observes @Any StudentRegisteredEvent event)
   {
      students.add(event.getStudent());
   }
   
   public Set<Student> getStudents()
   {
      return students;
   }
   
   public void reset()
   {
      students.clear();
   }
}
