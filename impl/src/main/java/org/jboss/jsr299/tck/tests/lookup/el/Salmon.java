package org.jboss.jsr299.tck.tests.lookup.el;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Named;

@Named @Default
@RequestScoped
public class Salmon
{
   private int age = 0;

   public int getAge()
   {
      return age;
   }

   public void setAge(int age)
   {
      this.age = age;
   }
}
