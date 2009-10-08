package org.jboss.jsr299.tck.tests.context.application;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class SimpleApplicationBean
{
   private double id = Math.random();

   public double getId()
   {
      return id;
   }
}
