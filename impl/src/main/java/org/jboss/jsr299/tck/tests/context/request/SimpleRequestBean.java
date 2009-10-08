package org.jboss.jsr299.tck.tests.context.request;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class SimpleRequestBean
{
   private double id = Math.random();

   public double getId()
   {
      return id;
   }
}
