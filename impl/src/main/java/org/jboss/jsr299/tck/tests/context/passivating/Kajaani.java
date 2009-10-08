package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class Kajaani implements Serializable
{
   private static final long serialVersionUID = 1L;
   private Integer theNumber = 0;

   public int getTheNumber()
   {
      return theNumber;
   }

   public void setTheNumber(int theNumber)
   {
      this.theNumber = theNumber;
   }
}
