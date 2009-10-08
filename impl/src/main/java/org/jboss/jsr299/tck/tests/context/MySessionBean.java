package org.jboss.jsr299.tck.tests.context;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
class MySessionBean implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private int id = 0;
   
   public void setId(int id)
   {
      this.id = id;
   }
   
   public int getId()
   {
      return id;
   }
   
   public void ping()
   {
   }

}
