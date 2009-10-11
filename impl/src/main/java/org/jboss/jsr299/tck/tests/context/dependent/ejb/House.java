package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped @Stateful
public class House implements HouseLocal
{
   
   public static boolean destroyed;
   
   @Inject RoomLocal room;
   
   public RoomLocal open() 
   {
      return room;
   }
   
   @PreDestroy
    public void preDestroy()
   {
      House.destroyed = true;
   }
   
}
