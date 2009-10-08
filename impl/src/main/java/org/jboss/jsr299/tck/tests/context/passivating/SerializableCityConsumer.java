package org.jboss.jsr299.tck.tests.context.passivating;

import javax.enterprise.context.RequestScoped;

@RequestScoped
class SerializableCityConsumer
{

   @Sleeping SerializableCity city;

   public void ping()
   {
   };

}
