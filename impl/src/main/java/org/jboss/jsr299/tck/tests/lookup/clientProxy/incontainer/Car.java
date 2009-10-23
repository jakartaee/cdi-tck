package org.jboss.jsr299.tck.tests.lookup.clientProxy.incontainer;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class Car implements Serializable
{
   private static final long serialVersionUID = -5928715726257481259L;
   private String make = "unknown";

   public String getMake()
   {
      return make;
   }

   public void setMake(String name)
   {
      this.make = name;
   }
}
