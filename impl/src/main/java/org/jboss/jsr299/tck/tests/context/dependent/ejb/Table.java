package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.annotation.PreDestroy;
import javax.ejb.Stateful;

@Stateful
public class Table implements TableLocal
{
   
   public static boolean destroyed;
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
   public void lay()
   {
      
   }
   
}
