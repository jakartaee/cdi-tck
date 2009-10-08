package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
class Generator implements Serializable
{
   
   private static final long serialVersionUID = -7213673465118041882L;
   
   private int number = 100;
   
   @Produces int getNumber()
   {
      return number;
   }
   
   @Produces @Sleeping SerializableCity getCity()
   {
      return new SerializableCity();
   }

}
