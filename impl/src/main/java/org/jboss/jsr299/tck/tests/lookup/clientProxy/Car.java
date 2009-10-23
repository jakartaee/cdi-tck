package org.jboss.jsr299.tck.tests.lookup.clientProxy;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class Car implements Serializable
{
   private static final long serialVersionUID = -8183045197385584658L;
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
