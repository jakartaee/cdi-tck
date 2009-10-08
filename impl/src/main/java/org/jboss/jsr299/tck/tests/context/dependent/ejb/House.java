package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped @Stateful
public class House implements HouseLocal
{
   
   @Inject RoomLocal room;
   
   public RoomLocal open() 
   {
      return room;
   };
   
}
