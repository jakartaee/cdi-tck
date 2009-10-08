package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;


public class Den
{
   private String name;
   
   public Den(String name)
   {
      this.name = name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }
}
