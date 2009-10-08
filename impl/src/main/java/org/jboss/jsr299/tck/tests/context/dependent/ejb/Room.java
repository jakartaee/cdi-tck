package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.inject.Inject;

@Stateful
public class Room implements RoomLocal
{
   
   @Inject TableLocal table;
   
   public TableLocal getTable()
   {
      return table;
   }
   
   public static boolean destroyed;
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
}
